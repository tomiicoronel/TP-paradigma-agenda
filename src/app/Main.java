package app;

/*
Contexto: App de agenda accesible para pacientes con problemas de memoria.
Stack: Java SE, Swing, JDBC, H2. Patrones: MVC + DAO + Observer.
Tarea: Bootstrap de app, init de DB, apertura de UI.
*/

import infra.db.ConexionDB;
import infra.db.VerificarDB;
import ui.CLI;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Agenda Accesible - Iniciando ===");

        // init schema si no existe
        ConexionDB.initSchemaIfAbsent();

        // Verificar que las tablas se crearon correctamente
        VerificarDB.verificarTablas();

        // TODO: lanzar scheduler (controller.TomaService)
        // new TomaService().start();

        // Lanzar CLI
        System.out.println("→ Lanzando interfaz de línea de comandos...\n");
        CLI cli = new CLI();
        cli.iniciar();

        System.out.println("=== Aplicación finalizada ===");
    }
}
