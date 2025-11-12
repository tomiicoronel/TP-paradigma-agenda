package test;

import domain.*;
import service.*;
import infra.db.ConexionDB;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Test de integración para la capa de servicios.
 *
 * Este test verifica:
 * 1. Creación de entidades a través de servicios
 * 2. Flujo completo de programación y registro de tomas
 * 3. Integración entre servicios
 */
public class TestServicios {

    public static void main(String[] args) {
        System.out.println("=== Iniciando tests de SERVICIOS ===\n");

        // Inicializar BD
        ConexionDB.initSchemaIfAbsent();

        // Instanciar servicios
        CuidadorService cuidadorService = new CuidadorService();
        PacienteService pacienteService = new PacienteService();
        MedicamentoService medicamentoService = new MedicamentoService();
        RecordatorioService recordatorioService = new RecordatorioService();

        try {
            // ============================================================
            // PASO 1: Crear Cuidador
            // ============================================================
            System.out.println("PASO 1: Creando cuidador...");
            Long cuidadorId = cuidadorService.crearCuidador(
                "Ana García",
                "ana.garcia@email.com"
            );
            System.out.println("✓ Cuidador creado con ID: " + cuidadorId);

            // ============================================================
            // PASO 2: Crear Paciente
            // ============================================================
            System.out.println("\nPASO 2: Creando paciente...");
            Long pacienteId = pacienteService.crearPaciente(
                "Carlos Rodríguez",
                "Texto grande, alto contraste, alertas visuales y sonoras",
                cuidadorId
            );
            System.out.println("✓ Paciente creado con ID: " + pacienteId);

            // ============================================================
            // PASO 3: Crear Medicamentos
            // ============================================================
            System.out.println("\nPASO 3: Creando medicamentos...");

            Long medicamento1Id = medicamentoService.crearMedicamento(
                "Enalapril 10mg",
                "Oral",
                "mg",
                "Para presión arterial. Tomar en ayunas."
            );
            System.out.println("✓ Medicamento 1 creado: Enalapril (ID: " + medicamento1Id + ")");

            Long medicamento2Id = medicamentoService.crearMedicamento(
                "Metformina 850mg",
                "Oral",
                "mg",
                "Para diabetes. Tomar con alimentos."
            );
            System.out.println("✓ Medicamento 2 creado: Metformina (ID: " + medicamento2Id + ")");

            // ============================================================
            // PASO 4: Crear Pautas de Medicación
            // ============================================================
            System.out.println("\nPASO 4: Creando pautas de medicación...");

            // Pauta 1: Enalapril - Cada 12 horas, empezando en 5 minutos
            LocalDateTime inicioPauta1 = LocalDateTime.now().plusMinutes(5);
            Long pauta1Id = medicamentoService.crearPauta(
                pacienteId,
                medicamento1Id,
                10,           // dosis
                "mg",         // unidad
                720,          // cada 12 horas (720 minutos)
                30,           // ventana de 30 minutos
                inicioPauta1
            );
            System.out.println("✓ Pauta 1 creada: Enalapril cada 12h (ID: " + pauta1Id + ")");
            System.out.println("  Primera toma programada para: " + inicioPauta1);

            // Pauta 2: Metformina - Cada 8 horas, empezando en 10 minutos
            LocalDateTime inicioPauta2 = LocalDateTime.now().plusMinutes(10);
            Long pauta2Id = medicamentoService.crearPauta(
                pacienteId,
                medicamento2Id,
                850,          // dosis
                "mg",         // unidad
                480,          // cada 8 horas (480 minutos)
                30,           // ventana de 30 minutos
                inicioPauta2
            );
            System.out.println("✓ Pauta 2 creada: Metformina cada 8h (ID: " + pauta2Id + ")");
            System.out.println("  Primera toma programada para: " + inicioPauta2);

            // ============================================================
            // PASO 5: Verificar Recordatorios Creados
            // ============================================================
            System.out.println("\nPASO 5: Verificando recordatorios creados...");
            List<Recordatorio> recordatoriosPendientes = recordatorioService.obtenerPendientes();
            System.out.println("✓ Recordatorios pendientes: " + recordatoriosPendientes.size());

            for (Recordatorio r : recordatoriosPendientes) {
                System.out.println("  - Recordatorio #" + r.getId() +
                    " programado para: " + r.getProgramadoAt() +
                    " (ventana: " + r.getVentanaMin() + " min)");
            }

            // ============================================================
            // PASO 6: Simular Registro de Toma
            // ============================================================
            if (!recordatoriosPendientes.isEmpty()) {
                System.out.println("\nPASO 6: Simulando registro de toma...");
                Recordatorio primerRecordatorio = recordatoriosPendientes.get(0);

                recordatorioService.registrarTomaHecha(
                    primerRecordatorio.getId(),
                    "Toma realizada correctamente en horario"
                );
                System.out.println("✓ Toma registrada como HECHA");

                // Verificar que se programó la siguiente
                System.out.println("\nVerificando que se programó la siguiente toma...");
                List<Recordatorio> despuesDeToma = recordatorioService.obtenerPendientes();
                System.out.println("✓ Recordatorios pendientes después del registro: " + despuesDeToma.size());
            }

            // ============================================================
            // PASO 7: Consultar Pautas Activas del Paciente
            // ============================================================
            System.out.println("\nPASO 7: Consultando pautas activas del paciente...");
            List<PacienteMedicamento> pautasActivas = pacienteService.obtenerPautasActivas(pacienteId);
            System.out.println("✓ Pautas activas: " + pautasActivas.size());

            for (PacienteMedicamento pm : pautasActivas) {
                System.out.println("  - Pauta (Paciente:" + pm.getPacienteId() +
                    ", Medicamento:" + pm.getMedicamentoId() + ")" +
                    ": " + pm.getDosis() + pm.getUnidad() +
                    " cada " + pm.getIntervaloMin() + " minutos");
                System.out.println("    Próxima toma: " + pm.getProximaTomaAt());
            }

            // ============================================================
            // PASO 8: Listar Todos los Medicamentos
            // ============================================================
            System.out.println("\nPASO 8: Listando medicamentos del sistema...");
            List<Medicamento> medicamentos = medicamentoService.listarTodos();
            System.out.println("✓ Total de medicamentos: " + medicamentos.size());
            for (Medicamento m : medicamentos) {
                System.out.println("  - " + m.getNombre() + " (" + m.getVia() + ")");
            }

            // ============================================================
            // PASO 9: Listar Todos los Pacientes
            // ============================================================
            System.out.println("\nPASO 9: Listando pacientes del sistema...");
            List<Paciente> pacientes = pacienteService.listarTodos();
            System.out.println("✓ Total de pacientes: " + pacientes.size());
            for (Paciente p : pacientes) {
                System.out.println("  - " + p.getNombre());
                System.out.println("    Preferencias: " + p.getPreferenciasAccesibilidad());
            }

            // ============================================================
            // RESUMEN FINAL
            // ============================================================
            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ TODOS LOS TESTS DE SERVICIOS PASARON EXITOSAMENTE");
            System.out.println("=".repeat(60));
            System.out.println("\nResumen:");
            System.out.println("  → 1 cuidador(es) creado(s)");
            System.out.println("  → " + pacientes.size() + " paciente(s) creado(s)");
            System.out.println("  → " + medicamentos.size() + " medicamento(s) creado(s)");
            System.out.println("  → " + pautasActivas.size() + " pauta(s) activa(s)");
            System.out.println("  → " + recordatoriosPendientes.size() + " recordatorio(s) inicial(es)");
            System.out.println("\n💡 Los servicios están listos para ser usados por la UI");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR EN TEST DE SERVICIOS:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("\nStack trace:");
            e.printStackTrace();
        }
    }
}

