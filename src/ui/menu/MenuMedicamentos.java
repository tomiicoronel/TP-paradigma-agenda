package ui.menu;

import domain.Medicamento;
import infra.dao.MedicamentoDAO;
import infra.dao.impl.MedicamentoDAOImpl;
import ui.utils.InputHelper;

import java.util.List;
import java.util.Scanner;

/**
 * Menú para gestionar Medicamentos.
 */
public class MenuMedicamentos {
    private final Scanner scanner;
    private final MedicamentoDAO medicamentoDAO;

    public MenuMedicamentos(Scanner scanner) {
        this.scanner = scanner;
        this.medicamentoDAO = new MedicamentoDAOImpl();
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n═══ 💊 GESTIÓN DE MEDICAMENTOS ═══");
            System.out.println("1. Listar Medicamentos");
            System.out.println("2. Crear Medicamento");
            System.out.println("3. Ver Detalle de Medicamento");
            System.out.println("0. ← Volver al menú principal");
            System.out.println("═══════════════════════════════════");
            System.out.print("Opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> listarMedicamentos();
                case 2 -> crearMedicamento();
                case 3 -> verDetalleMedicamento();
                case 0 -> volver = true;
                default -> System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void listarMedicamentos() {
        System.out.println("\n📋 LISTA DE MEDICAMENTOS:");
        System.out.println("─".repeat(80));
        List<Medicamento> medicamentos = medicamentoDAO.findAll();
        if (medicamentos.isEmpty()) {
            System.out.println("No hay medicamentos registrados.");
        } else {
            System.out.printf("%-5s %-30s %-15s %-20s%n",
                "ID", "Nombre Comercial", "Droga", "Presentación");
            System.out.println("─".repeat(80));
            for (Medicamento m : medicamentos) {
                System.out.printf("%-5d %-30s %-15s %-20s%n",
                    m.getId(),
                    m.getNombreComercial(),
                    m.getDroga() != null ? m.getDroga() : "N/A",
                    m.getPresentacion() != null ? m.getPresentacion() : "N/A");
            }
        }
        InputHelper.pausar(scanner);
    }

    private void crearMedicamento() {
        System.out.println("\n➕ CREAR NUEVO MEDICAMENTO:");
        System.out.println("─".repeat(50));

        String nombreComercial = InputHelper.leerString(scanner, "Nombre comercial: ");
        String droga = InputHelper.leerStringOpcional(scanner, "Droga (principio activo, opcional): ");
        String presentacion = InputHelper.leerStringOpcional(scanner, "Presentación (ej: comprimido 500mg, opcional): ");

        Medicamento medicamento = new Medicamento();
        medicamento.setNombreComercial(nombreComercial);
        medicamento.setDroga(droga.isEmpty() ? null : droga);
        medicamento.setPresentacion(presentacion.isEmpty() ? null : presentacion);

        medicamentoDAO.save(medicamento);
        System.out.println("✅ Medicamento creado exitosamente con ID: " + medicamento.getId());
        InputHelper.pausar(scanner);
    }

    private void verDetalleMedicamento() {
        System.out.println("\n🔍 VER DETALLE DE MEDICAMENTO:");
        Long id = (long) InputHelper.leerEnteroPositivo(scanner, "ID del medicamento: ");

        medicamentoDAO.findById(id).ifPresentOrElse(medicamento -> {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("  INFORMACIÓN DEL MEDICAMENTO");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.println("  ID:                  " + medicamento.getId());
            System.out.println("  Nombre Comercial:    " + medicamento.getNombreComercial());
            System.out.println("  Droga:               " + (medicamento.getDroga() != null ? medicamento.getDroga() : "N/A"));
            System.out.println("  Presentación:        " + (medicamento.getPresentacion() != null ? medicamento.getPresentacion() : "N/A"));

            InputHelper.pausar(scanner);
        }, () -> {
            System.out.println("❌ Medicamento no encontrado.");
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

