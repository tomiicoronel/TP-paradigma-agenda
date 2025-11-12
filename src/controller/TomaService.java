package controller;

/*
Contexto: Scheduler de notificaciones y control de estados de recordatorios.
Stack: Java SE, ScheduledExecutorService, JDBC.
Patrones: Observer (Subject), Singleton (opcional).
Tarea: Monitoreo periódico de recordatorios pendientes/aplazados, emisión de notificaciones.
*/

import domain.Notificacion;
import domain.Recordatorio;
import domain.Adherencia;
import infra.dao.RecordatorioDAO;
import infra.dao.NotificacionDAO;
import infra.dao.AdherenciaDAO;
import infra.dao.impl.RecordatorioDAOImpl;
import infra.dao.impl.NotificacionDAOImpl;
import infra.dao.impl.AdherenciaDAOImpl;
import shared.observer.Observer;
import shared.observer.Subject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Servicio central de monitoreo de recordatorios.
 *
 * Responsabilidades:
 * 1. Verificar cada minuto si hay recordatorios que requieren notificación.
 * 2. Cambiar estados (PENDIENTE → APLAZADO → PERDIDO) según ventanas de tiempo.
 * 3. Emitir notificaciones y registrar en Adherencia.
 * 4. Notificar a observers (UI) cuando hay cambios relevantes.
 *
 * Ciclo de vida:
 * - start(): inicia el scheduler
 * - stop(): detiene el scheduler de forma ordenada
 */
public class TomaService implements Subject {

    // DAOs
    private final RecordatorioDAO recordatorioDAO;
    private final NotificacionDAO notificacionDAO;
    private final AdherenciaDAO adherenciaDAO;

    // Scheduler
    private ScheduledExecutorService scheduler;
    private static final int INTERVALO_VERIFICACION_SEG = 60; // Cada 1 minuto

    // Observer pattern
    private final List<Observer> observers = new ArrayList<>();

    // Control de ejecución
    private boolean running = false;

    // Configuración de ventanas (minutos)
    private static final int MINUTOS_ANTES_NOTIFICAR = 5; // Notificar 5 min antes
    private static final int MINUTOS_RECUPERACION = 60;   // 1 hora para recuperar aplazados

    public TomaService() {
        this.recordatorioDAO = new RecordatorioDAOImpl();
        this.notificacionDAO = new NotificacionDAOImpl();
        this.adherenciaDAO = new AdherenciaDAOImpl();
    }

    /**
     * Inicia el scheduler de verificación periódica.
     */
    public synchronized void start() {
        if (running) {
            System.out.println("⚠️  TomaService ya está en ejecución.");
            return;
        }

        System.out.println("🚀 Iniciando TomaService...");
        scheduler = Executors.newScheduledThreadPool(1);

        // Ejecutar verificación inmediata y luego cada minuto
        scheduler.scheduleAtFixedRate(
            this::verificarRecordatorios,
            0, // delay inicial = 0
            INTERVALO_VERIFICACION_SEG,
            TimeUnit.SECONDS
        );

        running = true;
        System.out.println("✅ TomaService activo. Verificando cada " + INTERVALO_VERIFICACION_SEG + " segundos.");
    }

    /**
     * Detiene el scheduler de forma ordenada.
     */
    public synchronized void stop() {
        if (!running) {
            System.out.println("⚠️  TomaService ya estaba detenido.");
            return;
        }

        System.out.println("🛑 Deteniendo TomaService...");
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        running = false;
        System.out.println("✅ TomaService detenido.");
    }

    /**
     * Lógica principal de verificación.
     * Se ejecuta periódicamente por el scheduler.
     */
    private void verificarRecordatorios() {
        try {
            LocalDateTime ahora = LocalDateTime.now();

            // 1. Procesar recordatorios PENDIENTES
            procesarPendientes(ahora);

            // 2. Procesar recordatorios APLAZADOS
            procesarAplazados(ahora);

        } catch (Exception e) {
            System.err.println("❌ Error en verificarRecordatorios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Procesa recordatorios en estado PENDIENTE.
     * - Si llegó la hora (o está dentro de MINUTOS_ANTES_NOTIFICAR): emite notificación.
     * - Si expiró la ventana: pasa a APLAZADO.
     */
    private void procesarPendientes(LocalDateTime ahora) {
        List<Recordatorio> pendientes = recordatorioDAO.findByEstado("PENDIENTE");

        for (Recordatorio r : pendientes) {
            LocalDateTime programado = r.getProgramadoAt();
            LocalDateTime limiteVentana = programado.plusMinutes(r.getVentanaMin());

            // ¿Ya expiró la ventana?
            if (ahora.isAfter(limiteVentana)) {
                pasarAAplazado(r, ahora);
                continue;
            }

            // ¿Está dentro del rango de notificación?
            LocalDateTime momentoNotificar = programado.minusMinutes(MINUTOS_ANTES_NOTIFICAR);

            if (ahora.isAfter(momentoNotificar) || ahora.isEqual(momentoNotificar)) {
                emitirNotificacion(r, ahora);
            }
        }
    }

    /**
     * Procesa recordatorios en estado APLAZADO.
     * - Si superó el tiempo de recuperación: pasa a PERDIDO.
     */
    private void procesarAplazados(LocalDateTime ahora) {
        List<Recordatorio> aplazados = recordatorioDAO.findByEstado("APLAZADO");

        for (Recordatorio r : aplazados) {
            LocalDateTime programado = r.getProgramadoAt();
            LocalDateTime limiteRecuperacion = programado.plusMinutes(MINUTOS_RECUPERACION);

            if (ahora.isAfter(limiteRecuperacion)) {
                pasarAPerdido(r, ahora);
            }
        }
    }

    /**
     * Cambia un recordatorio a estado APLAZADO.
     */
    private void pasarAAplazado(Recordatorio r, LocalDateTime ahora) {
        long minutosRetraso = ChronoUnit.MINUTES.between(
            r.getProgramadoAt().plusMinutes(r.getVentanaMin()),
            ahora
        );

        String motivo = "Expiró ventana de tolerancia (" + r.getVentanaMin() + " min). Retraso: " + minutosRetraso + " min.";

        recordatorioDAO.pasarAplazado(r.getId(), motivo);

        // Registrar en adherencia
        registrarAdherencia(r.getId(), "APLAZADO", motivo, ahora);

        System.out.println("⏰ Recordatorio #" + r.getId() + " → APLAZADO (" + motivo + ")");

        notifyObservers(); // Notificar a UI
    }

    /**
     * Cambia un recordatorio a estado PERDIDO.
     */
    private void pasarAPerdido(Recordatorio r, LocalDateTime ahora) {
        long minutosRetraso = ChronoUnit.MINUTES.between(r.getProgramadoAt(), ahora);

        String motivo = "Superó límite de recuperación (" + MINUTOS_RECUPERACION + " min). Retraso total: " + minutosRetraso + " min.";

        recordatorioDAO.pasarPerdido(r.getId(), motivo);

        // Registrar en adherencia
        registrarAdherencia(r.getId(), "PERDIDO", motivo, ahora);

        System.out.println("❌ Recordatorio #" + r.getId() + " → PERDIDO (" + motivo + ")");

        notifyObservers(); // Notificar a UI
    }

    /**
     * Emite una notificación para un recordatorio.
     * Solo emite si no hay notificación previa entregada.
     */
    private void emitirNotificacion(Recordatorio r, LocalDateTime ahora) {
        // Verificar si ya se notificó
        List<Notificacion> notifs = notificacionDAO.findByRecordatorio(r.getId());
        boolean yaNotificado = notifs.stream().anyMatch(Notificacion::isEntregada);

        if (yaNotificado) {
            return; // Ya se notificó anteriormente
        }

        // Crear notificación
        Notificacion n = new Notificacion();
        n.setRecordatorioId(r.getId());
        n.setEmitidaAt(ahora);
        n.setCanalVisual("POPUP"); // Podría ser configurable
        n.setCanalSonoro("BEEP");  // Podría ser configurable
        n.setEntregada(true);

        notificacionDAO.crear(n);

        // Registrar en adherencia
        registrarAdherencia(r.getId(), "NOTIFICADO",
            "Notificación emitida a las " + ahora.toLocalTime(), ahora);

        System.out.println("🔔 Notificación emitida para Recordatorio #" + r.getId() +
            " (programado: " + r.getProgramadoAt().toLocalTime() + ")");

        notifyObservers(); // Notificar a UI para mostrar alerta
    }

    /**
     * Registra una acción en la tabla de adherencia.
     */
    private void registrarAdherencia(Long recordatorioId, String accion, String descripcion, LocalDateTime timestamp) {
        Adherencia a = new Adherencia();
        a.setRecordatorioId(recordatorioId);
        a.setAccion(accion);
        a.setObservaciones(descripcion);
        a.setRegistradaAt(timestamp);

        adherenciaDAO.registrarAccion(a);
    }

    // ==================== Observer Pattern ====================

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
            System.out.println("👀 Observer registrado en TomaService.");
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
        System.out.println("👋 Observer removido de TomaService.");
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    // ==================== Getters ====================

    public boolean isRunning() {
        return running;
    }
}

