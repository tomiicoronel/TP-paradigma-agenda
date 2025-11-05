package infra.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Utilidad para verificar que las tablas se crearon correctamente.
 * Ejecutar desde Main o como test manual.
 */
public class VerificarDB {

    public static void verificarTablas() {
        System.out.println("\n=== Verificando estructura de BD ===");

        String[] tablasEsperadas = {
            "CUIDADOR", "PACIENTE", "RUTINA", "ITEM_RUTINA",
            "MEDICAMENTO", "PACIENTE_MEDICAMENTO",
            "RECORDATORIO", "NOTIFICACION", "ADHERENCIA"
        };

        try (Connection cn = ConexionDB.getConnection()) {
            DatabaseMetaData meta = cn.getMetaData();

            for (String tabla : tablasEsperadas) {
                try (ResultSet rs = meta.getTables(null, null, tabla, new String[]{"TABLE"})) {
                    if (rs.next()) {
                        System.out.println("✓ Tabla " + tabla + " existe");

                        // Contar columnas
                        try (ResultSet cols = meta.getColumns(null, null, tabla, null)) {
                            int count = 0;
                            while (cols.next()) count++;
                            System.out.println("  → " + count + " columnas");
                        }
                    } else {
                        System.out.println("✗ Tabla " + tabla + " NO EXISTE");
                    }
                }
            }

            System.out.println("=== Verificación completada ===\n");

        } catch (Exception e) {
            System.err.println("Error verificando BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Test manual
    public static void main(String[] args) {
        ConexionDB.initSchemaIfAbsent();
        verificarTablas();
    }
}

