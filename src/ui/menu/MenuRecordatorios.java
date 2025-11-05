package ui.menu;

import domain.Recordatorio;
import domain.PacienteMedicamento;
import domain.Paciente;
import domain.Medicamento;
import infra.dao.RecordatorioDAO;
import infra.dao.PacienteMedicamentoDAO;
import infra.dao.PacienteDAO;
import infra.dao.MedicamentoDAO;
import infra.dao.impl.RecordatorioDAOImpl;
import infra.dao.impl.PacienteMedicamentoDAOImpl;
import infra.dao.impl.PacienteDAOImpl;
import infra.dao.impl.MedicamentoDAOImpl;
import ui.utils.InputHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Menú para visualizar y gestionar recordatorios pendientes.
 */
public class MenuRecordatorios {
    private final Scanner scanner;
    private final RecordatorioDAO recordatorioDAO;
    private final PacienteMedicamentoDAO pautaDAO;
    private final PacienteDAO pacienteDAO;
    private final MedicamentoDAO medicamentoDAO;

    private static final DateTimeFormatter DATETIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MenuRecordatorios(Scanner scanner) {
        this.scanner = scanner;
        this.recordatorioDAO = new RecordatorioDAOImpl();
        this.pautaDAO = new PacienteMedicamentoDAOImpl();
        this.pacienteDAO = new PacienteDAOImpl();
        this.medicamentoDAO = new MedicamentoDAOImpl();
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n═══ ⏰ RECORDATORIOS ═══");
            System.out.println("1. Ver Recordatorios Pendientes");
            System.out.println("2. Ver Recordatorios de un Paciente");
            System.out.println("3. Marcar Recordatorio como HECHO");
            System.out.println("4. Ver Todos los Recordatorios");
            System.out.println("0. ← Volver al menú principal");
            System.out.println("═══════════════════════════════════");
            System.out.print("Opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> verRecordatoriosPendientes();
                case 2 -> verRecordatoriosPaciente();
                case 3 -> marcarComoHecho();
                case 4 -> verTodosRecordatorios();
                case 0 -> volver = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void verRecordatoriosPendientes() {
        System.out.println("\n📋 RECORDATORIOS PENDIENTES:");
        System.out.println("─".repeat(100));

        List<Recordatorio> recordatorios = recordatorioDAO.findByEstado("PENDIENTE");

        if (recordatorios.isEmpty()) {
            System.out.println("✓ No hay recordatorios pendientes.");
        } else {
            System.out.printf("%-5s %-25s %-25s %-20s %-15s%n",
                "ID", "Paciente", "Medicamento", "Programado Para", "Estado");
            System.out.println("─".repeat(100));

            for (Recordatorio r : recordatorios) {
                mostrarRecordatorio(r);
            }
        }
        InputHelper.pausar(scanner);
    }

    private void verRecordatoriosPaciente() {
        System.out.println("\n📋 RECORDATORIOS DE UN PACIENTE:");
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        pacienteDAO.findById(pacienteId).ifPresentOrElse(paciente -> {
            System.out.println("\n═══ Recordatorios de: " + paciente.getNombre() + " ═══");

            List<Recordatorio> recordatorios = recordatorioDAO.findByPacienteId(pacienteId);

            if (recordatorios.isEmpty()) {
                System.out.println("No hay recordatorios para este paciente.");
            } else {
                System.out.println("─".repeat(100));
                System.out.printf("%-5s %-25s %-20s %-15s %-20s%n",
                    "ID", "Medicamento", "Programado Para", "Estado", "Realizado En");
                System.out.println("─".repeat(100));

                for (Recordatorio r : recordatorios) {
                    mostrarRecordatorioDetalle(r);
                }
            }
            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Paciente no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private void marcarComoHecho() {
        System.out.println("\n✓ MARCAR RECORDATORIO COMO HECHO:");
        Long recordatorioId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del recordatorio: ");

        recordatorioDAO.findById(recordatorioId).ifPresentOrElse(recordatorio -> {
            if ("HECHO".equals(recordatorio.getEstado())) {
                System.out.println("⚠️  Este recordatorio ya fue marcado como HECHO.");
                InputHelper.pausar(scanner);
                return;
            }

            // Actualizar estado
            LocalDateTime ahora = LocalDateTime.now();
            recordatorio.setEstado("HECHO");
            recordatorio.setRealizadoAt(ahora);
            recordatorioDAO.update(recordatorio);

            System.out.println("✅ Recordatorio marcado como HECHO exitosamente.");
            System.out.println("   Hora de realización: " + ahora.format(DATETIME_FORMATTER));

            // TODO: Aquí debería dispararse la lógica de reprogramación
            // y actualización de adherencia (cuando implementemos el Service)

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Recordatorio no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private void verTodosRecordatorios() {
        System.out.println("\n📋 TODOS LOS RECORDATORIOS:");
        System.out.println("─".repeat(100));

        List<Recordatorio> recordatorios = recordatorioDAO.findAll();

        if (recordatorios.isEmpty()) {
            System.out.println("No hay recordatorios registrados.");
        } else {
            System.out.printf("%-5s %-25s %-25s %-20s %-15s%n",
                "ID", "Paciente", "Medicamento", "Programado Para", "Estado");
            System.out.println("─".repeat(100));

            for (Recordatorio r : recordatorios) {
                mostrarRecordatorio(r);
            }
        }
        InputHelper.pausar(scanner);
    }

    private void mostrarRecordatorio(Recordatorio r) {
        Long pacienteId = r.getPacienteId();
        Long medicamentoId = r.getMedicamentoId();

        String nombrePaciente = "N/A";
        String nombreMedicamento = "N/A";

        if (pacienteId != null) {
            Paciente paciente = pacienteDAO.findById(pacienteId).orElse(null);
            if (paciente != null) {
                nombrePaciente = paciente.getNombre();
            }
        }

        if (medicamentoId != null) {
            Medicamento medicamento = medicamentoDAO.findById(medicamentoId).orElse(null);
            if (medicamento != null) {
                nombreMedicamento = medicamento.getNombreComercial();
            }
        }

        System.out.printf("%-5d %-25s %-25s %-20s %-15s%n",
            r.getId(),
            nombrePaciente,
            nombreMedicamento,
            r.getProgramadoAt().format(DATETIME_FORMATTER),
            r.getEstado());
    }

    private void mostrarRecordatorioDetalle(Recordatorio r) {
        Long medicamentoId = r.getMedicamentoId();
        String nombreMedicamento = "N/A";

        if (medicamentoId != null) {
            Medicamento medicamento = medicamentoDAO.findById(medicamentoId).orElse(null);
            if (medicamento != null) {
                nombreMedicamento = medicamento.getNombreComercial();
            }
        }

        String realizadoEn = r.getRealizadoAt() != null ?
            r.getRealizadoAt().format(DATETIME_FORMATTER) : "N/A";

        System.out.printf("%-5d %-25s %-20s %-15s %-20s%n",
            r.getId(),
            nombreMedicamento,
            r.getProgramadoAt().format(DATETIME_FORMATTER),
            r.getEstado(),
            realizadoEn);
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

