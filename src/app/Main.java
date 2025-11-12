package app;

/*
Contexto: App de agenda accesible para pacientes con problemas de memoria.
Stack: Java SE, Swing, JDBC, H2. Patrones: MVC + DAO + Observer.
Tarea: Bootstrap de app, init de DB, apertura de UI.
*/

import controller.TomaService;
import infra.db.ConexionDB;
import infra.db.VerificarDB;
import ui.CLI;
import ui.MainFrame;

import javax.swing.*;

public class Main {
    private static TomaService tomaService;

    public static void main(String[] args) {
        System.out.println("=== Agenda Accesible - Iniciando ===");

        // init schema si no existe
        ConexionDB.initSchemaIfAbsent();

        // Verificar que las tablas se crearon correctamente
        VerificarDB.verificarTablas();

        // Iniciar scheduler de notificaciones
        System.out.println("→ Iniciando servicio de recordatorios...");
        tomaService = new TomaService();
        tomaService.start();

        // Registrar shutdown hook para detener el servicio limpiamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n→ Cerrando servicios...");
            if (tomaService != null && tomaService.isRunning()) {
                tomaService.stop();
            }
        }));

        // Decidir entre CLI o GUI
        if (args.length > 0 && args[0].equals("--cli")) {
            // Lanzar CLI
            System.out.println("→ Lanzando interfaz de línea de comandos...\n");
            CLI cli = new CLI();
            cli.iniciar();
            System.out.println("=== Aplicación finalizada ===");
        } else {
            // Lanzar GUI (Swing)
            System.out.println("→ Lanzando interfaz gráfica...\n");
            SwingUtilities.invokeLater(() -> {
                try {
                    // Configurar Look and Feel del sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainFrame frame = new MainFrame(tomaService);
                frame.setVisible(true);

                System.out.println("✅ Interfaz gráfica iniciada correctamente.");
                System.out.println("💡 Tip: Usa 'java -jar app.jar --cli' para iniciar en modo CLI.");
            });
        }
    }

    /**
     * Permite acceso al TomaService desde otros componentes (ej: CLI, Swing UI).
     */
    public static TomaService getTomaService() {
        return tomaService;
    }
}


