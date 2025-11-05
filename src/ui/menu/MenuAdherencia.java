package ui.menu;

import domain.Adherencia;
import domain.Recordatorio;
import domain.Paciente;
import domain.Medicamento;
import infra.dao.AdherenciaDAO;
import infra.dao.RecordatorioDAO;
import infra.dao.PacienteDAO;
import infra.dao.MedicamentoDAO;
import infra.dao.impl.AdherenciaDAOImpl;
import infra.dao.impl.RecordatorioDAOImpl;
import infra.dao.impl.PacienteDAOImpl;
import infra.dao.impl.MedicamentoDAOImpl;
import ui.utils.InputHelper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Menú para visualizar el historial de adherencia.
 */
public class MenuAdherencia {
    private final Scanner scanner;
    private final AdherenciaDAO adherenciaDAO;
    private final RecordatorioDAO recordatorioDAO;
    private final PacienteDAO pacienteDAO;
    private final MedicamentoDAO medicamentoDAO;

    private static final DateTimeFormatter DATETIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MenuAdherencia(Scanner scanner) {
        this.scanner = scanner;
        this.adherenciaDAO = new AdherenciaDAOImpl();
        this.recordatorioDAO = new RecordatorioDAOImpl();
        this.pacienteDAO = new PacienteDAOImpl();
        this.medicamentoDAO = new MedicamentoDAOImpl();
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n═══ 📊 HISTORIAL DE ADHERENCIA ═══");
            System.out.println("1. Ver Adherencia de un Paciente");
            System.out.println("2. Ver Adherencia por Recordatorio");
            System.out.println("3. Ver Toda la Adherencia");
            System.out.println("4. Estadísticas de Adherencia");
            System.out.println("0. ← Volver al menú principal");
            System.out.println("═══════════════════════════════════");
            System.out.print("Opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> verAdherenciaPaciente();
                case 2 -> verAdherenciaRecordatorio();
                case 3 -> verTodaAdherencia();
                case 4 -> verEstadisticas();
                case 0 -> volver = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void verAdherenciaPaciente() {
        System.out.println("\n📊 VER ADHERENCIA DE UN PACIENTE:");
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        pacienteDAO.findById(pacienteId).ifPresentOrElse(paciente -> {
            System.out.println("\n═══ Historial de adherencia: " + paciente.getNombre() + " ═══");

            // Obtener todos los recordatorios del paciente
            List<Recordatorio> recordatorios = recordatorioDAO.findByPacienteId(pacienteId);

            if (recordatorios.isEmpty()) {
                System.out.println("No hay recordatorios para este paciente.");
                InputHelper.pausar(scanner);
                return;
            }

            System.out.println("─".repeat(110));
            System.out.printf("%-5s %-25s %-20s %-15s %-20s %-15s%n",
                "ID", "Medicamento", "Programado", "Estado Final", "Realizado", "Observaciones");
            System.out.println("─".repeat(110));

            for (Recordatorio r : recordatorios) {
                List<Adherencia> adherencias = adherenciaDAO.findByRecordatorioId(r.getId());

                String nombreMed = "N/A";
                if (r.getMedicamentoId() != null) {
                    Medicamento med = medicamentoDAO.findById(r.getMedicamentoId()).orElse(null);
                    if (med != null) nombreMed = med.getNombreComercial();
                }

                String realizadoEn = r.getRealizadoAt() != null ?
                    r.getRealizadoAt().format(DATETIME_FORMATTER) : "N/A";

                String observaciones = "";
                if (!adherencias.isEmpty()) {
                    Adherencia ultima = adherencias.get(adherencias.size() - 1);
                    observaciones = ultima.getObservaciones() != null ? ultima.getObservaciones() : "";
                }

                System.out.printf("%-5d %-25s %-20s %-15s %-20s %-15s%n",
                    r.getId(),
                    nombreMed.length() > 25 ? nombreMed.substring(0, 22) + "..." : nombreMed,
                    r.getProgramadoAt().format(DATETIME_FORMATTER),
                    r.getEstado(),
                    realizadoEn,
                    observaciones.length() > 15 ? observaciones.substring(0, 12) + "..." : observaciones);
            }

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Paciente no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private void verAdherenciaRecordatorio() {
        System.out.println("\n📊 VER ADHERENCIA POR RECORDATORIO:");
        Long recordatorioId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del recordatorio: ");

        recordatorioDAO.findById(recordatorioId).ifPresentOrElse(recordatorio -> {
            System.out.println("\n═══ Historial del Recordatorio #" + recordatorioId + " ═══");

            List<Adherencia> adherencias = adherenciaDAO.findByRecordatorioId(recordatorioId);

            if (adherencias.isEmpty()) {
                System.out.println("No hay registros de adherencia para este recordatorio.");
            } else {
                System.out.println("─".repeat(100));
                System.out.printf("%-5s %-20s %-15s %-15s %-30s%n",
                    "ID", "Fecha/Hora", "Estado Previo", "Estado Nuevo", "Observaciones");
                System.out.println("─".repeat(100));

                for (Adherencia a : adherencias) {
                    String obs = a.getObservaciones() != null ? a.getObservaciones() : "N/A";
                    System.out.printf("%-5d %-20s %-15s %-15s %-30s%n",
                        a.getId(),
                        a.getRegistradoAt().format(DATETIME_FORMATTER),
                        a.getEstadoPrevio() != null ? a.getEstadoPrevio() : "N/A",
                        a.getEstadoNuevo(),
                        obs.length() > 30 ? obs.substring(0, 27) + "..." : obs);
                }
            }

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Recordatorio no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private void verTodaAdherencia() {
        System.out.println("\n📊 TODA LA ADHERENCIA REGISTRADA:");
        System.out.println("─".repeat(110));

        List<Adherencia> adherencias = adherenciaDAO.findAll();

        if (adherencias.isEmpty()) {
            System.out.println("No hay registros de adherencia.");
        } else {
            System.out.printf("%-5s %-12s %-20s %-15s %-15s %-30s%n",
                "ID", "Recordat.", "Fecha/Hora", "Estado Previo", "Estado Nuevo", "Observaciones");
            System.out.println("─".repeat(110));

            for (Adherencia a : adherencias) {
                String obs = a.getObservaciones() != null ? a.getObservaciones() : "N/A";
                System.out.printf("%-5d %-12d %-20s %-15s %-15s %-30s%n",
                    a.getId(),
                    a.getRecordatorioId(),
                    a.getRegistradoAt().format(DATETIME_FORMATTER),
                    a.getEstadoPrevio() != null ? a.getEstadoPrevio() : "N/A",
                    a.getEstadoNuevo(),
                    obs.length() > 30 ? obs.substring(0, 27) + "..." : obs);
            }
        }

        InputHelper.pausar(scanner);
    }

    private void verEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DE ADHERENCIA:");
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        pacienteDAO.findById(pacienteId).ifPresentOrElse(paciente -> {
            System.out.println("\n═══ Estadísticas de: " + paciente.getNombre() + " ═══");

            List<Recordatorio> recordatorios = recordatorioDAO.findByPacienteId(pacienteId);

            if (recordatorios.isEmpty()) {
                System.out.println("No hay datos suficientes para generar estadísticas.");
                InputHelper.pausar(scanner);
                return;
            }

            int total = recordatorios.size();
            long hechos = recordatorios.stream().filter(r -> "HECHO".equals(r.getEstado())).count();
            long perdidos = recordatorios.stream().filter(r -> "PERDIDO".equals(r.getEstado())).count();
            long pendientes = recordatorios.stream().filter(r -> "PENDIENTE".equals(r.getEstado())).count();
            long aplazados = recordatorios.stream().filter(r -> "APLAZADO".equals(r.getEstado())).count();

            double porcentajeAdherencia = total > 0 ? (hechos * 100.0 / total) : 0.0;

            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("  Total de recordatorios:    " + total);
            System.out.println("  ✓ Hechos:                  " + hechos);
            System.out.println("  ⏰ Pendientes:              " + pendientes);
            System.out.println("  ⏳ Aplazados:               " + aplazados);
            System.out.println("  ✗ Perdidos:                " + perdidos);
            System.out.println("  ─────────────────────────────────────────────────");
            System.out.printf("  📈 Adherencia:              %.1f%%%n", porcentajeAdherencia);
            System.out.println("╚═══════════════════════════════════════════════════╝");

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Paciente no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

