package ui.menu;

import domain.Paciente;
import domain.Cuidador;
import infra.dao.PacienteDAO;
import infra.dao.CuidadorDAO;
import infra.dao.impl.PacienteDAOImpl;
import infra.dao.impl.CuidadorDAOImpl;
import ui.utils.InputHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Menú para gestionar Pacientes y Cuidadores.
 */
public class MenuPacientes {
    private final Scanner scanner;
    private final PacienteDAO pacienteDAO;
    private final CuidadorDAO cuidadorDAO;

    public MenuPacientes(Scanner scanner) {
        this.scanner = scanner;
        this.pacienteDAO = new PacienteDAOImpl();
        this.cuidadorDAO = new CuidadorDAOImpl();
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n═══ 👤 GESTIÓN DE PACIENTES Y CUIDADORES ═══");
            System.out.println("1. Listar Cuidadores");
            System.out.println("2. Crear Cuidador");
            System.out.println("3. Listar Pacientes");
            System.out.println("4. Crear Paciente");
            System.out.println("5. Ver Detalle de Paciente");
            System.out.println("0. ← Volver al menú principal");
            System.out.println("═════════════════════════════════════════════");
            System.out.print("Opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> listarCuidadores();
                case 2 -> crearCuidador();
                case 3 -> listarPacientes();
                case 4 -> crearPaciente();
                case 5 -> verDetallePaciente();
                case 0 -> volver = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarCuidadores() {
        System.out.println("\n📋 LISTA DE CUIDADORES:");
        System.out.println("─".repeat(70));
        List<Cuidador> cuidadores = cuidadorDAO.findAll();
        if (cuidadores.isEmpty()) {
            System.out.println("No hay cuidadores registrados.");
        } else {
            System.out.printf("%-5s %-25s %-30s %-10s%n", "ID", "Nombre", "Email", "Teléfono");
            System.out.println("─".repeat(70));
            for (Cuidador c : cuidadores) {
                System.out.printf("%-5d %-25s %-30s %-10s%n",
                    c.getId(),
                    c.getNombre(),
                    c.getEmail() != null ? c.getEmail() : "N/A",
                    c.getTelefono() != null ? c.getTelefono() : "N/A");
            }
        }
        InputHelper.pausar(scanner);
    }

    private void crearCuidador() {
        System.out.println("\n➕ CREAR NUEVO CUIDADOR:");
        System.out.println("─".repeat(50));

        String nombre = InputHelper.leerString(scanner, "Nombre completo: ");
        String email = InputHelper.leerStringOpcional(scanner, "Email (opcional): ");
        String telefono = InputHelper.leerStringOpcional(scanner, "Teléfono (opcional): ");

        Cuidador cuidador = new Cuidador();
        cuidador.setNombre(nombre);
        cuidador.setEmail(email.isEmpty() ? null : email);
        cuidador.setTelefono(telefono.isEmpty() ? null : telefono);

        cuidadorDAO.save(cuidador);
        System.out.println("✅ Cuidador creado exitosamente con ID: " + cuidador.getId());
        InputHelper.pausar(scanner);
    }

    private void listarPacientes() {
        System.out.println("\n📋 LISTA DE PACIENTES:");
        System.out.println("─".repeat(90));
        List<Paciente> pacientes = pacienteDAO.findAll();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
        } else {
            System.out.printf("%-5s %-25s %-15s %-20s %-10s%n",
                "ID", "Nombre", "Fecha Nac.", "Diagnóstico", "Cuidador");
            System.out.println("─".repeat(90));
            for (Paciente p : pacientes) {
                Cuidador cuidador = p.getCuidadorId() != null ?
                    cuidadorDAO.findById(p.getCuidadorId()).orElse(null) : null;
                System.out.printf("%-5d %-25s %-15s %-20s %-10s%n",
                    p.getId(),
                    p.getNombre(),
                    InputHelper.formatearFecha(p.getFechaNacimiento()),
                    p.getDiagnostico() != null ? p.getDiagnostico() : "N/A",
                    cuidador != null ? cuidador.getNombre() : "Sin cuidador");
            }
        }
        InputHelper.pausar(scanner);
    }

    private void crearPaciente() {
        System.out.println("\n➕ CREAR NUEVO PACIENTE:");
        System.out.println("─".repeat(50));

        String nombre = InputHelper.leerString(scanner, "Nombre completo: ");
        LocalDate fechaNac = InputHelper.leerFecha(scanner, "Fecha de nacimiento");
        String diagnostico = InputHelper.leerStringOpcional(scanner, "Diagnóstico (opcional): ");

        // Mostrar cuidadores disponibles
        List<Cuidador> cuidadores = cuidadorDAO.findAll();
        if (cuidadores.isEmpty()) {
            System.out.println("⚠️  No hay cuidadores registrados. Créelo primero.");
            InputHelper.pausar(scanner);
            return;
        }

        System.out.println("\nCuidadores disponibles:");
        for (Cuidador c : cuidadores) {
            System.out.printf("  [%d] %s%n", c.getId(), c.getNombre());
        }

        Long cuidadorId = (long) InputHelper.leerEnteroPositivo(scanner, "ID del cuidador responsable: ");

        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setFechaNacimiento(fechaNac);
        paciente.setDiagnostico(diagnostico.isEmpty() ? null : diagnostico);
        paciente.setCuidadorId(cuidadorId);

        pacienteDAO.save(paciente);
        System.out.println("✅ Paciente creado exitosamente con ID: " + paciente.getId());
        InputHelper.pausar(scanner);
    }

    private void verDetallePaciente() {
        System.out.println("\n🔍 VER DETALLE DE PACIENTE:");
        Long id = (long) InputHelper.leerEnteroPositivo(scanner, "ID del paciente: ");

        pacienteDAO.findById(id).ifPresentOrElse(paciente -> {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("  INFORMACIÓN DEL PACIENTE");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.println("  ID:              " + paciente.getId());
            System.out.println("  Nombre:          " + paciente.getNombre());
            System.out.println("  Fecha Nac.:      " + InputHelper.formatearFecha(paciente.getFechaNacimiento()));
            System.out.println("  Diagnóstico:     " + (paciente.getDiagnostico() != null ? paciente.getDiagnostico() : "N/A"));

            if (paciente.getCuidadorId() != null) {
                cuidadorDAO.findById(paciente.getCuidadorId()).ifPresent(cuidador -> {
                    System.out.println("\n  --- Cuidador Responsable ---");
                    System.out.println("  Nombre:          " + cuidador.getNombre());
                    System.out.println("  Email:           " + (cuidador.getEmail() != null ? cuidador.getEmail() : "N/A"));
                    System.out.println("  Teléfono:        " + (cuidador.getTelefono() != null ? cuidador.getTelefono() : "N/A"));
                });
            }

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

