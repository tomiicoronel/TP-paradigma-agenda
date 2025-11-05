package ui;

import ui.menu.*;
import java.util.Scanner;

/**
 * Interfaz de línea de comandos para la Agenda Accesible.
 * Permite gestionar pacientes, medicamentos, recordatorios y adherencia.
 */
public class CLI {
    private final Scanner scanner;
    private final MenuPacientes menuPacientes;
    private final MenuMedicamentos menuMedicamentos;
    private final MenuPautas menuPautas;
    private final MenuRecordatorios menuRecordatorios;
    private final MenuAdherencia menuAdherencia;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.menuPacientes = new MenuPacientes(scanner);
        this.menuMedicamentos = new MenuMedicamentos(scanner);
        this.menuPautas = new MenuPautas(scanner);
        this.menuRecordatorios = new MenuRecordatorios(scanner);
        this.menuAdherencia = new MenuAdherencia(scanner);
    }

    public void iniciar() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║     AGENDA ACCESIBLE - Sistema de Recordatorios  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    menuPacientes.mostrar();
                    break;
                case 2:
                    menuMedicamentos.mostrar();
                    break;
                case 3:
                    menuPautas.mostrar();
                    break;
                case 4:
                    menuRecordatorios.mostrar();
                    break;
                case 5:
                    menuAdherencia.mostrar();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n¡Hasta pronto! Cerrando aplicación...\n");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Por favor intente nuevamente.\n");
            }
        }

        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("═══════════════ MENÚ PRINCIPAL ═══════════════");
        System.out.println("1. 👤 Gestión de Pacientes y Cuidadores");
        System.out.println("2. 💊 Gestión de Medicamentos");
        System.out.println("3. 📋 Configuración de Pautas (Paciente-Medicamento)");
        System.out.println("4. ⏰ Recordatorios Pendientes");
        System.out.println("5. 📊 Historial de Adherencia");
        System.out.println("0. 🚪 Salir");
        System.out.println("═══════════════════════════════════════════════");
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

