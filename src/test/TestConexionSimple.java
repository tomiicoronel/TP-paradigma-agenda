package test;

import infra.db.ConexionDB;
import java.sql.*;

public class TestConexionSimple {
    public static void main(String[] args) {
        System.out.println("=== Test de Verificación Completa ===\n");
        ConexionDB.initSchemaIfAbsent();
        
        try (Connection cn = ConexionDB.getConnection()) {

            // Test 1: Insertar y leer Cuidador
            System.out.println("1. Test Cuidador:");
            PreparedStatement ps1 = cn.prepareStatement(
                "INSERT INTO cuidador (nombre, contacto) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps1.setString(1, "María González");
            ps1.setString(2, "maria@email.com");
            ps1.executeUpdate();
            ResultSet rs1 = ps1.getGeneratedKeys();
            Long cuidadorId = rs1.next() ? rs1.getLong(1) : null;
            System.out.println("   ✓ Cuidador creado con ID: " + cuidadorId);

            // Test 2: Insertar Paciente
            System.out.println("\n2. Test Paciente:");
            PreparedStatement ps2 = cn.prepareStatement(
                "INSERT INTO paciente (nombre, preferencias_accesibilidad, cuidador_id) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps2.setString(1, "Juan Pérez");
            ps2.setString(2, "Fuente grande, contraste alto");
            ps2.setLong(3, cuidadorId);
            ps2.executeUpdate();
            ResultSet rs2 = ps2.getGeneratedKeys();
            Long pacienteId = rs2.next() ? rs2.getLong(1) : null;
            System.out.println("   ✓ Paciente creado con ID: " + pacienteId);

            // Test 3: Insertar Medicamento
            System.out.println("\n3. Test Medicamento:");
            PreparedStatement ps3 = cn.prepareStatement(
                "INSERT INTO medicamento (nombre, via, unidad_dosis, notas) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps3.setString(1, "Ibuprofeno");
            ps3.setString(2, "Oral");
            ps3.setString(3, "mg");
            ps3.setString(4, "Tomar con alimentos");
            ps3.executeUpdate();
            ResultSet rs3 = ps3.getGeneratedKeys();
            Long medicamentoId = rs3.next() ? rs3.getLong(1) : null;
            System.out.println("   ✓ Medicamento creado con ID: " + medicamentoId);

            // Test 4: Crear Pauta Paciente-Medicamento
            System.out.println("\n4. Test Pauta Medicación:");
            PreparedStatement ps4 = cn.prepareStatement(
                "INSERT INTO paciente_medicamento (paciente_id, medicamento_id, dosis, unidad, intervalo_min, ventana_min, activo) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            ps4.setLong(1, pacienteId);
            ps4.setLong(2, medicamentoId);
            ps4.setDouble(3, 400);
            ps4.setString(4, "mg");
            ps4.setInt(5, 480); // 8 horas
            ps4.setInt(6, 30);
            ps4.setBoolean(7, true);
            ps4.executeUpdate();
            System.out.println("   ✓ Pauta de medicación creada");

            // Test 5: Crear Recordatorio
            System.out.println("\n5. Test Recordatorio:");
            PreparedStatement ps5 = cn.prepareStatement(
                "INSERT INTO recordatorio (paciente_id, tipo, referencia_tipo, referencia_id, programado_at, ventana_min, estado) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps5.setLong(1, pacienteId);
            ps5.setString(2, "MEDICACION");
            ps5.setString(3, "PAC_MED");
            ps5.setLong(4, medicamentoId);
            ps5.setInt(5, 30);
            ps5.setString(6, "PENDIENTE");
            ps5.executeUpdate();
            ResultSet rs5 = ps5.getGeneratedKeys();
            Long recordatorioId = rs5.next() ? rs5.getLong(1) : null;
            System.out.println("   ✓ Recordatorio creado con ID: " + recordatorioId);

            // Test 6: Verificar tablas
            System.out.println("\n6. Resumen de datos:");
            Statement st = cn.createStatement();

            ResultSet rsCuidadores = st.executeQuery("SELECT COUNT(*) FROM cuidador");
            rsCuidadores.next();
            System.out.println("   - Cuidadores: " + rsCuidadores.getInt(1));

            ResultSet rsPacientes = st.executeQuery("SELECT COUNT(*) FROM paciente");
            rsPacientes.next();
            System.out.println("   - Pacientes: " + rsPacientes.getInt(1));

            ResultSet rsMedicamentos = st.executeQuery("SELECT COUNT(*) FROM medicamento");
            rsMedicamentos.next();
            System.out.println("   - Medicamentos: " + rsMedicamentos.getInt(1));

            ResultSet rsRecordatorios = st.executeQuery("SELECT COUNT(*) FROM recordatorio");
            rsRecordatorios.next();
            System.out.println("   - Recordatorios: " + rsRecordatorios.getInt(1));

            System.out.println("\n✅ ¡TODOS LOS TESTS PASARON EXITOSAMENTE!");
            System.out.println("✅ Base de datos H2 funcionando correctamente");
            System.out.println("✅ Todas las tablas operativas");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

