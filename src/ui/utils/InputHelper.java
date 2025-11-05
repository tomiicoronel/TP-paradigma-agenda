package ui.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Helper para leer y validar entradas del usuario.
 */
public class InputHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Lee un String no vacío.
     */
    public static String leerString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("❌ El campo no puede estar vacío. " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    /**
     * Lee un String que puede ser vacío (opcional).
     */
    public static String leerStringOpcional(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Lee un entero positivo.
     */
    public static int leerEnteroPositivo(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor > 0) {
                    return valor;
                }
                System.out.println("❌ Debe ser un número positivo.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Ingrese un número.");
            }
        }
    }

    /**
     * Lee un entero (puede ser 0 o negativo).
     */
    public static int leerEntero(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Ingrese un número.");
            }
        }
    }

    /**
     * Lee una fecha en formato dd/MM/yyyy.
     */
    public static LocalDate leerFecha(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (dd/MM/yyyy): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Use dd/MM/yyyy (ej: 15/03/2025)");
            }
        }
    }

    /**
     * Lee una hora en formato HH:mm.
     */
    public static LocalTime leerHora(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (HH:mm): ");
                String input = scanner.nextLine().trim();
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Use HH:mm (ej: 14:30)");
            }
        }
    }

    /**
     * Lee un booleano (S/N).
     */
    public static boolean leerBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (S/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("S") || input.equals("SI")) {
                return true;
            } else if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            System.out.println("❌ Respuesta inválida. Use S o N.");
        }
    }

    /**
     * Pausa hasta que el usuario presione Enter.
     */
    public static void pausar(Scanner scanner) {
        System.out.print("\n[Presione ENTER para continuar]");
        scanner.nextLine();
    }

    /**
     * Formatea una fecha para mostrar.
     */
    public static String formatearFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(DATE_FORMATTER) : "N/A";
    }

    /**
     * Formatea una hora para mostrar.
     */
    public static String formatearHora(LocalTime hora) {
        return hora != null ? hora.format(TIME_FORMATTER) : "N/A";
    }
}

