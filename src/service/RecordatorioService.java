package service;

import domain.*;
import infra.dao.*;
import infra.dao.impl.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de recordatorios y adherencia.
 *
 * Responsabilidades:
 * - Consulta de recordatorios pendientes/aplazados/perdidos
 * - Registro de acciones del usuario (HECHO, APLAZADO, CANCELADO)
 * - Gestión de notificaciones
 * - Consulta de historial de adherencia
 */
public class RecordatorioService {

    private final RecordatorioDAO recordatorioDAO;
    private final NotificacionDAO notificacionDAO;
    private final AdherenciaDAO adherenciaDAO;
    private final PacienteMedicamentoDAO pmDAO;
    private final MedicamentoService medicamentoService;

    public RecordatorioService() {
        this.recordatorioDAO = new RecordatorioDAOImpl();
        this.notificacionDAO = new NotificacionDAOImpl();
        this.adherenciaDAO = new AdherenciaDAOImpl();
        this.pmDAO = new PacienteMedicamentoDAOImpl();
        this.medicamentoService = new MedicamentoService();
    }

    /**
     * Obtiene todos los recordatorios pendientes del sistema.
     */
    public List<Recordatorio> obtenerPendientes() {
        return recordatorioDAO.findByEstado("PENDIENTE");
    }

    /**
     * Obtiene recordatorios por estado específico.
     */
    public List<Recordatorio> obtenerPorEstado(String estado) {
        return recordatorioDAO.findByEstado(estado);
    }

    /**
     * Obtiene recordatorios de un paciente específico.
     */
    public List<Recordatorio> obtenerPorPaciente(Long pacienteId) {
        return recordatorioDAO.findByPacienteId(pacienteId);
    }

    /**
     * Registra que el usuario completó una toma.
     * Marca el recordatorio como HECHO y programa la siguiente toma.
     */
    public void registrarTomaHecha(Long recordatorioId, String observaciones) {
        Optional<Recordatorio> recOpt = recordatorioDAO.findById(recordatorioId);
        if (!recOpt.isPresent()) {
            throw new IllegalArgumentException("Recordatorio no encontrado");
        }

        Recordatorio recordatorio = recOpt.get();

        // Registrar adherencia
        Adherencia adherencia = new Adherencia();
        adherencia.setRecordatorioId(recordatorioId);
        adherencia.setRegistradaAt(LocalDateTime.now());
        adherencia.setAccion("HECHO");
        adherencia.setObservaciones(observaciones != null ? observaciones : "Toma completada");
        adherenciaDAO.registrarAccion(adherencia);

        // Marcar recordatorio como HECHO
        recordatorioDAO.marcarHecho(recordatorioId);

        // Programar siguiente toma si es medicación recurrente
        if ("MEDICACION".equals(recordatorio.getTipo())) {
            programarSiguienteToma(recordatorio);
        }

        System.out.println("✅ Toma registrada como HECHA para recordatorio #" + recordatorioId);
    }

    /**
     * Registra que el usuario aplazó una toma.
     */
    public void registrarTomaAplazada(Long recordatorioId, String motivo) {
        // Registrar adherencia
        Adherencia adherencia = new Adherencia();
        adherencia.setRecordatorioId(recordatorioId);
        adherencia.setRegistradaAt(LocalDateTime.now());
        adherencia.setAccion("APLAZADO");
        adherencia.setObservaciones(motivo != null ? motivo : "Usuario aplazó manualmente");
        adherenciaDAO.registrarAccion(adherencia);

        // Marcar recordatorio como APLAZADO
        recordatorioDAO.pasarAplazado(recordatorioId, motivo);

        System.out.println("⏰ Recordatorio #" + recordatorioId + " aplazado por usuario");
    }

    /**
     * Registra que el usuario canceló una toma.
     */
    public void registrarTomaCancelada(Long recordatorioId, String motivo) {
        // Registrar adherencia
        Adherencia adherencia = new Adherencia();
        adherencia.setRecordatorioId(recordatorioId);
        adherencia.setRegistradaAt(LocalDateTime.now());
        adherencia.setAccion("CANCELADO");
        adherencia.setObservaciones(motivo != null ? motivo : "Usuario canceló");
        adherenciaDAO.registrarAccion(adherencia);

        // TODO: Agregar método marcarCancelado al DAO o usar pasarPerdido
        recordatorioDAO.pasarPerdido(recordatorioId, motivo != null ? motivo : "Cancelado por usuario");

        System.out.println("❌ Recordatorio #" + recordatorioId + " cancelado por usuario");
    }

    /**
     * Programa la siguiente toma basándose en la pauta asociada.
     */
    private void programarSiguienteToma(Recordatorio recordatorio) {
        // Si el recordatorio está asociado a una pauta de medicación
        if ("PAC_MED".equals(recordatorio.getReferenciaTipo())) {
            // Buscar la pauta correspondiente al paciente y medicamento
            List<PacienteMedicamento> pautas = pmDAO.findActivosByPaciente(recordatorio.getPacienteId());

            for (PacienteMedicamento pauta : pautas) {
                if (pauta.getMedicamentoId().equals(recordatorio.getReferenciaId())) {
                    // Programar siguiente toma usando MedicamentoService
                    medicamentoService.programarSiguienteToma(
                        pauta.getPacienteId(),
                        pauta.getMedicamentoId()
                    );
                    break;
                }
            }
        }
    }

    /**
     * Obtiene el historial de adherencia de un paciente.
     */
    public List<Adherencia> obtenerHistorialAdherencia(Long pacienteId, LocalDateTime desde, LocalDateTime hasta) {
        return adherenciaDAO.findByPacienteRangoFechas(pacienteId, desde, hasta);
    }

    /**
     * Obtiene las notificaciones de un recordatorio.
     */
    public List<Notificacion> obtenerNotificaciones(Long recordatorioId) {
        return notificacionDAO.findByRecordatorio(recordatorioId);
    }

    /**
     * Crea una notificación manual para un recordatorio.
     */
    public void crearNotificacion(Long recordatorioId, String canalVisual, String canalSonoro) {
        Notificacion notificacion = new Notificacion();
        notificacion.setRecordatorioId(recordatorioId);
        notificacion.setEmitidaAt(LocalDateTime.now());
        notificacion.setCanalVisual(canalVisual);
        notificacion.setCanalSonoro(canalSonoro);
        notificacion.setEntregada(true);

        notificacionDAO.crear(notificacion);

        System.out.println("🔔 Notificación manual creada para recordatorio #" + recordatorioId);
    }

    /**
     * Obtiene un recordatorio por ID.
     */
    public Optional<Recordatorio> obtenerRecordatorio(Long id) {
        return recordatorioDAO.findById(id);
    }

    /**
     * Lista todos los recordatorios del día actual.
     * Incluye PENDIENTES, APLAZADOS y HECHOS de hoy.
     */
    public List<Recordatorio> listarRecordatoriosHoy() {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return recordatorioDAO.findByRangoFechas(inicioDia, finDia);
    }

    /**
     * Marca un recordatorio como HECHO (versión simplificada para UI).
     */
    public void marcarHecho(Long recordatorioId) {
        registrarTomaHecha(recordatorioId, "Marcado desde interfaz");
    }

    /**
     * Aplaza un recordatorio X minutos (versión simplificada para UI).
     */
    public void aplazarRecordatorio(Long recordatorioId, int minutos) {
        registrarTomaAplazada(recordatorioId, "Aplazado " + minutos + " minutos desde interfaz");

        // Reprogramar el recordatorio
        Optional<Recordatorio> recOpt = recordatorioDAO.findById(recordatorioId);
        if (recOpt.isPresent()) {
            Recordatorio rec = recOpt.get();
            LocalDateTime nuevaHora = LocalDateTime.now().plusMinutes(minutos);

            // Actualizar la hora programada
            recordatorioDAO.actualizarHoraProgramada(recordatorioId, nuevaHora);
            // Cambiar estado de vuelta a PENDIENTE
            recordatorioDAO.cambiarEstado(recordatorioId, "PENDIENTE");
        }
    }

    /**
     * Cancela un recordatorio (versión simplificada para UI).
     */
    public void cancelarRecordatorio(Long recordatorioId) {
        registrarTomaCancelada(recordatorioId, "Cancelado desde interfaz");
    }
}

