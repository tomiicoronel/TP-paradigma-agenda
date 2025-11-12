package test;

import controller.TomaService;
import domain.Recordatorio;
import infra.dao.RecordatorioDAO;
import infra.dao.impl.RecordatorioDAOImpl;
import infra.db.ConexionDB;
import shared.observer.Observer;

import java.time.LocalDateTime;

/**
 * Test de demostración del TomaService.
 * Crea recordatorios de prueba y observa cómo el scheduler los procesa.
 */
public class TestTomaService {

    public static void main(String[] args) {
        System.out.println("=== Test TomaService ===\n");

        // Inicializar BD
        ConexionDB.initSchemaIfAbsent();

        // Crear recordatorios de prueba
        crearRecordatoriosDePrueba();

        // Crear TomaService
        TomaService tomaService = new TomaService();

        // Registrar un observer simple
        tomaService.addObserver(new Observer() {
            @Override
            public void update() {
                System.out.println(">>> Observer notificado: hubo cambios en recordatorios!");
            }
        });

        // Iniciar el servicio
        tomaService.start();

        System.out.println("\n🕐 TomaService corriendo... (se detendrá en 2 minutos)\n");
        System.out.println("Esperando notificaciones automáticas...\n");

        // Dejar correr el servicio por 2 minutos para observar
        try {
            Thread.sleep(120000); // 2 minutos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detener el servicio
        tomaService.stop();

        System.out.println("\n=== Test finalizado ===");
    }

    /**
     * Crea recordatorios de prueba en diferentes estados.
     */
    private static void crearRecordatoriosDePrueba() {
        RecordatorioDAO dao = new RecordatorioDAOImpl();

        // Recordatorio 1: Próximo (en 2 minutos) - debería notificarse
        Recordatorio r1 = new Recordatorio();
        r1.setPacienteId(1L);
        r1.setMedicamentoId(1L);
        r1.setTipo("MEDICACION");
        r1.setReferenciaTipo("PAC_MED");
        r1.setReferenciaId(1L);
        r1.setProgramadoAt(LocalDateTime.now().plusMinutes(2));
        r1.setVentanaMin(15);
        r1.setEstado("PENDIENTE");
        r1.setMotivoEstado("Recordatorio de prueba - próximo");

        Long id1 = dao.crearPendiente(r1);
        System.out.println("✓ Recordatorio #" + id1 + " creado: en 2 minutos");

        // Recordatorio 2: Vencido (hace 20 min) - debería pasar a APLAZADO
        Recordatorio r2 = new Recordatorio();
        r2.setPacienteId(1L);
        r2.setMedicamentoId(2L);
        r2.setTipo("MEDICACION");
        r2.setReferenciaTipo("PAC_MED");
        r2.setReferenciaId(2L);
        r2.setProgramadoAt(LocalDateTime.now().minusMinutes(20));
        r2.setVentanaMin(10);
        r2.setEstado("PENDIENTE");
        r2.setMotivoEstado("Recordatorio de prueba - vencido");

        Long id2 = dao.crearPendiente(r2);
        System.out.println("✓ Recordatorio #" + id2 + " creado: hace 20 min (debería pasar a APLAZADO)");

        // Recordatorio 3: Muy vencido (hace 2 horas) - debería pasar a PERDIDO
        Recordatorio r3 = new Recordatorio();
        r3.setPacienteId(1L);
        r3.setMedicamentoId(3L);
        r3.setTipo("MEDICACION");
        r3.setReferenciaTipo("PAC_MED");
        r3.setReferenciaId(3L);
        r3.setProgramadoAt(LocalDateTime.now().minusMinutes(120));
        r3.setVentanaMin(10);
        r3.setEstado("APLAZADO");
        r3.setMotivoEstado("Recordatorio de prueba - muy vencido");

        dao.crearPendiente(r3);
        // Actualizar manualmente a APLAZADO (simulando que ya pasó por esa etapa)
        dao.pasarAplazado(r3.getId(), "Test - simular aplazado antiguo");

        System.out.println("✓ Recordatorio #" + r3.getId() + " creado: hace 2 horas (debería pasar a PERDIDO)");

        System.out.println();
    }
}

