package test;

import domain.*;
import infra.dao.*;
import infra.dao.impl.*;
import infra.db.ConexionDB;

import java.time.LocalDateTime;

/**
 * Test manual de los DAOs implementados.
 * Ejecutar este main para probar CRUD básico.
 */
public class TestDAOs {

    public static void main(String[] args) {
        System.out.println("=== Iniciando tests de DAOs ===\n");

        // Inicializar BD
        ConexionDB.initSchemaIfAbsent();

        // Instanciar DAOs
        CuidadorDAO cuidadorDAO = new CuidadorDAOImpl();
        PacienteDAO pacienteDAO = new PacienteDAOImpl();
        MedicamentoDAO medicamentoDAO = new MedicamentoDAOImpl();
        PacienteMedicamentoDAO pmDAO = new PacienteMedicamentoDAOImpl();
        RecordatorioDAO recordatorioDAO = new RecordatorioDAOImpl();
        NotificacionDAO notificacionDAO = new NotificacionDAOImpl();
        AdherenciaDAO adherenciaDAO = new AdherenciaDAOImpl();

        try {
            // 1. Crear Cuidador
            System.out.println("→ Creando cuidador...");
            Cuidador cuidador = new Cuidador();
            cuidador.setNombre("María González");
            cuidador.setContacto("maria@email.com");
            Long cuidadorId = cuidadorDAO.save(cuidador);
            System.out.println("✓ Cuidador creado con ID: " + cuidadorId);

            // 2. Crear Paciente
            System.out.println("\n→ Creando paciente...");
            Paciente paciente = new Paciente();
            paciente.setNombre("Juan Pérez");
            paciente.setPreferenciasAccesibilidad("Fuente grande, alto contraste");
            paciente.setCuidadorId(cuidadorId);
            Long pacienteId = pacienteDAO.save(paciente);
            System.out.println("✓ Paciente creado con ID: " + pacienteId);

            // 3. Crear Medicamento
            System.out.println("\n→ Creando medicamento...");
            Medicamento medicamento = new Medicamento();
            medicamento.setNombre("Ibuprofeno");
            medicamento.setVia("Oral");
            medicamento.setUnidadDosis("mg");
            medicamento.setNotas("Tomar con alimentos");
            Long medicamentoId = medicamentoDAO.save(medicamento);
            System.out.println("✓ Medicamento creado con ID: " + medicamentoId);

            // 4. Crear Pauta PacienteMedicamento
            System.out.println("\n→ Creando pauta de medicación...");
            PacienteMedicamento pm = new PacienteMedicamento();
            pm.setPacienteId(pacienteId);
            pm.setMedicamentoId(medicamentoId);
            pm.setDosis(400);
            pm.setUnidad("mg");
            pm.setIntervaloMin(480); // 8 horas
            pm.setVentanaMin(30);
            pm.setHoraInicio(LocalDateTime.now());
            pm.setProximaTomaAt(LocalDateTime.now().plusHours(8));
            pm.setActivo(true);
            pmDAO.savePauta(pm);
            System.out.println("✓ Pauta creada");

            // 5. Crear Recordatorio
            System.out.println("\n→ Creando recordatorio...");
            Recordatorio recordatorio = new Recordatorio();
            recordatorio.setPacienteId(pacienteId);
            recordatorio.setTipo("MEDICACION");
            recordatorio.setReferenciaTipo("PAC_MED");
            recordatorio.setReferenciaId(medicamentoId);
            recordatorio.setProgramadoAt(LocalDateTime.now().plusMinutes(10));
            recordatorio.setVentanaMin(30);
            Long recordatorioId = recordatorioDAO.crearPendiente(recordatorio);
            System.out.println("✓ Recordatorio creado con ID: " + recordatorioId);

            // 6. Crear Notificación
            System.out.println("\n→ Creando notificación...");
            Notificacion notificacion = new Notificacion();
            notificacion.setRecordatorioId(recordatorioId);
            notificacion.setEmitidaAt(LocalDateTime.now());
            notificacion.setCanalVisual("POPUP");
            notificacion.setCanalSonoro("BEEP");
            notificacion.setEntregada(false);
            Long notificacionId = notificacionDAO.crear(notificacion);
            System.out.println("✓ Notificación creada con ID: " + notificacionId);

            // 7. Registrar Adherencia
            System.out.println("\n→ Registrando adherencia...");
            Adherencia adherencia = new Adherencia();
            adherencia.setRecordatorioId(recordatorioId);
            adherencia.setRegistradaAt(LocalDateTime.now());
            adherencia.setAccion("HECHO");
            adherencia.setObservaciones("Tomado correctamente");
            Long adherenciaId = adherenciaDAO.registrarAccion(adherencia);
            System.out.println("✓ Adherencia registrada con ID: " + adherenciaId);

            // 8. Actualizar estado de recordatorio
            System.out.println("\n→ Marcando recordatorio como HECHO...");
            recordatorioDAO.marcarHecho(recordatorioId);
            System.out.println("✓ Recordatorio actualizado");

            // 9. Verificar lecturas
            System.out.println("\n→ Probando lecturas...");
            cuidadorDAO.findById(cuidadorId).ifPresent(c ->
                System.out.println("  Cuidador: " + c.getNombre())
            );

            pacienteDAO.findById(pacienteId).ifPresent(p ->
                System.out.println("  Paciente: " + p.getNombre())
            );

            medicamentoDAO.findById(medicamentoId).ifPresent(m ->
                System.out.println("  Medicamento: " + m.getNombre())
            );

            System.out.println("\n=== ✓ TODOS LOS TESTS PASARON EXITOSAMENTE ===");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR EN TEST: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();
        }
    }
}

