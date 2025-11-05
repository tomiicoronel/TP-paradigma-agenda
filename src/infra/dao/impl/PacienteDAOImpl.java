package infra.dao.impl;

import domain.Paciente;
import infra.dao.PacienteDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDAOImpl implements PacienteDAO {

    @Override
    public Long save(Paciente p) {
        String sql = "INSERT INTO paciente (nombre, preferencias_accesibilidad, cuidador_id) VALUES (?, ?, ?)";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getPreferenciasAccesibilidad());
            ps.setLong(3, p.getCuidadorId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    p.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se generó ID para paciente");
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Paciente p) {
        String sql = "UPDATE paciente SET nombre = ?, preferencias_accesibilidad = ?, cuidador_id = ? WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getPreferenciasAccesibilidad());
            ps.setLong(3, p.getCuidadorId());
            ps.setLong(4, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Paciente> findById(Long id) {
        String sql = "SELECT id, nombre, preferencias_accesibilidad, cuidador_id FROM paciente WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paciente> findByCuidador(Long cuidadorId) {
        String sql = "SELECT id, nombre, preferencias_accesibilidad, cuidador_id FROM paciente WHERE cuidador_id = ? ORDER BY nombre";
        List<Paciente> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, cuidadorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando pacientes por cuidador: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paciente> findAll() {
        String sql = "SELECT id, nombre, preferencias_accesibilidad, cuidador_id FROM paciente ORDER BY nombre";
        List<Paciente> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapRow(rs));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando pacientes: " + e.getMessage(), e);
        }
    }

    private Paciente mapRow(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPreferenciasAccesibilidad(rs.getString("preferencias_accesibilidad"));
        p.setCuidadorId(rs.getLong("cuidador_id"));
        return p;
    }
}
