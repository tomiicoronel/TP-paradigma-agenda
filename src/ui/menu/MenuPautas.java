package ui.menu;

import domain.PacienteMedicamento;
import domain.Paciente;
import domain.Medicamento;
import infra.dao.PacienteMedicamentoDAO;
import infra.dao.PacienteDAO;
import infra.dao.MedicamentoDAO;
import infra.dao.impl.PacienteMedicamentoDAOImpl;
import infra.dao.impl.PacienteDAOImpl;
import infra.dao.impl.MedicamentoDAOImpl;
import ui.utils.InputHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Menú para configurar pautas de medicación (PacienteMedicamento).
 */
public class MenuPautas {
    private final Scanner scanner;
    private final PacienteMedicamentoDAO pautaDAO;
    private final PacienteDAO pacienteDAO;
    private final MedicamentoDAO medicamentoDAO;

    public MenuPautas(Scanner scanner) {
        this.scanner = scanner;
        this.pautaDAO = new PacienteMedicamentoDAOImpl();
        this.pacienteDAO = new PacienteDAOImpl();
        this.medicamentoDAO = new MedicamentoDAOImpl();
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n═══ 📋 CONFIGURACIÓN DE PAUTAS ═══");
            System.out.println("1. Listar Pautas de un Paciente");
            System.out.println("2. Crear Nueva Pauta");
            System.out.println("3. Ver Detalle de Pauta");
            System.out.println("0. ← Volver al menú principal");
            System.out.println("═══════════════════════════════════");
            System.out.print("Opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> listarPautasPaciente();
                case 2 -> crearPauta();
                case 3 -> verDetallePauta();
                case 0 -> volver = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarPautasPaciente() {
        System.out.println("\n📋 LISTAR PAUTAS DE UN PACIENTE:");
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        pacienteDAO.findById(pacienteId).ifPresentOrElse(paciente -> {
            System.out.println("\n═══ Pautas de: " + paciente.getNombre() + " ═══");
            List<PacienteMedicamento> pautas = pautaDAO.findByPacienteId(pacienteId);

            if (pautas.isEmpty()) {
                System.out.println("No hay pautas configuradas para este paciente.");
            } else {
                System.out.println("─".repeat(90));
                System.out.printf("%-30s %-12s %-10s %-15s %-10s%n",
                    "Medicamento", "Hora Inicio", "Intervalo", "Dosis", "Activa");
                System.out.println("─".repeat(90));

                for (PacienteMedicamento pauta : pautas) {
                    medicamentoDAO.findById(pauta.getMedicamentoId()).ifPresent(med -> {
                        // Convertir LocalDateTime a LocalTime para mostrar solo la hora
                        String horaStr = pauta.getHoraInicio() != null ?
                            InputHelper.formatearHora(pauta.getHoraInicio().toLocalTime()) : "N/A";
                        System.out.printf("%-30s %-12s %-10d min %-15s %-10s%n",
                            med.getNombreComercial(),
                            horaStr,
                            pauta.getIntervaloMinutos(),
                            pauta.getUnidad() != null ? pauta.getUnidad() : "N/A",
                            pauta.isActiva() ? "✓ Sí" : "✗ No");
                    });
                }
            }
            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Paciente no encontrado.");
            InputHelper.pausar(scanner);
        });
    }

    private void crearPauta() {
        System.out.println("\n➕ CREAR NUEVA PAUTA:");
        System.out.println("─".repeat(50));

        // Seleccionar paciente
        List<Paciente> pacientes = pacienteDAO.findAll();
        if (pacientes.isEmpty()) {
            System.out.println("⚠️  No hay pacientes registrados.");
            InputHelper.pausar(scanner);
            return;
        }

        System.out.println("\nPacientes disponibles:");
        for (Paciente p : pacientes) {
            System.out.printf("  [%d] %s%n", p.getId(), p.getNombre());
        }
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        // Seleccionar medicamento
        List<Medicamento> medicamentos = medicamentoDAO.findAll();
        if (medicamentos.isEmpty()) {
            System.out.println("⚠️  No hay medicamentos registrados.");
            InputHelper.pausar(scanner);
            return;
        }

        System.out.println("\nMedicamentos disponibles:");
        for (Medicamento m : medicamentos) {
            System.out.printf("  [%d] %s%n", m.getId(), m.getNombreComercial());
        }
        Long medicamentoId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del medicamento: ");

        // Configurar pauta
        LocalTime horaInicio = InputHelper.leerHora(scanner, "Hora de inicio");
        int intervalo = InputHelper.leerEnteroPositivo(scanner, "Intervalo entre tomas (minutos): ");
        String dosis = InputHelper.leerStringOpcional(scanner, "Dosis (ej: 1 comprimido, opcional): ");
        boolean activa = InputHelper.leerBoolean(scanner, "¿Activar pauta ahora?");

        PacienteMedicamento pauta = new PacienteMedicamento();
        pauta.setPacienteId(pacienteId);
        pauta.setMedicamentoId(medicamentoId);
        // Convertir LocalTime a LocalDateTime (hoy a esa hora)
        pauta.setHoraInicio(LocalDateTime.of(LocalDate.now(), horaInicio));
        pauta.setIntervaloMinutos(intervalo);
        pauta.setVentanaMin(30); // Valor por defecto
        pauta.setDosis(dosis);
        pauta.setActiva(activa);

        pautaDAO.save(pauta);
        System.out.println("✅ Pauta creada exitosamente.");

        if (activa) {
            System.out.println("⏰ Se generarán recordatorios automáticamente según el intervalo configurado.");
        }

        InputHelper.pausar(scanner);
    }

    private void verDetallePauta() {
        System.out.println("\n🔍 VER DETALLE DE PAUTA:");
        Long pacienteId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");
        Long medicamentoId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del medicamento: ");

        pautaDAO.findById(pacienteId, medicamentoId).ifPresentOrElse(pauta -> {
            Paciente paciente = pacienteDAO.findById(pacienteId).orElse(null);
            Medicamento medicamento = medicamentoDAO.findById(medicamentoId).orElse(null);

            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("  DETALLE DE LA PAUTA");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.println("  Paciente:           " + (paciente != null ? paciente.getNombre() : "ID: " + pacienteId));
            System.out.println("  Medicamento:        " + (medicamento != null ? medicamento.getNombreComercial() : "ID: " + medicamentoId));

            String horaStr = pauta.getHoraInicio() != null ?
                InputHelper.formatearHora(pauta.getHoraInicio().toLocalTime()) : "N/A";
            System.out.println("  Hora de inicio:     " + horaStr);
            System.out.println("  Intervalo:          " + pauta.getIntervaloMinutos() + " minutos");
            System.out.println("  Dosis:              " + (pauta.getUnidad() != null ? pauta.getUnidad() : "N/A"));
            System.out.println("  Estado:             " + (pauta.isActiva() ? "✓ Activa" : "✗ Inactiva"));

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Pauta no encontrada.");
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

