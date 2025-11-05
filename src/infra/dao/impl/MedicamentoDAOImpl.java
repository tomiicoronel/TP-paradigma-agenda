package infra.dao.impl;

import domain.Medicamento;
import infra.dao.MedicamentoDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicamentoDAOImpl implements MedicamentoDAO {

    @Override
    public Long save(Medicamento m) {
        String sql = "INSERT INTO medicamento (nombre, via, unidad_dosis, notas) VALUES (?, ?, ?, ?)";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNombre());
            ps.setString(2, m.getVia());
            ps.setString(3, m.getUnidadDosis());
            ps.setString(4, m.getNotas());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    m.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se generó ID para medicamento");
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Medicamento m) {
        String sql = "UPDATE medicamento SET nombre = ?, via = ?, unidad_dosis = ?, notas = ? WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, m.getNombre());
            ps.setString(2, m.getVia());
            ps.setString(3, m.getUnidadDosis());
            ps.setString(4, m.getNotas());
            ps.setLong(5, m.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM medicamento WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Medicamento> findById(Long id) {
        String sql = "SELECT id, nombre, via, unidad_dosis, notas FROM medicamento WHERE id = ?";
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
            throw new RuntimeException("Error buscando medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Medicamento> searchByNombre(String nombre) {
        String sql = "SELECT id, nombre, via, unidad_dosis, notas FROM medicamento WHERE LOWER(nombre) LIKE ? ORDER BY nombre";
        List<Medicamento> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando medicamentos por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Medicamento> findAll() {
        String sql = "SELECT id, nombre, via, unidad_dosis, notas FROM medicamento ORDER BY nombre";
        List<Medicamento> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapRow(rs));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando medicamentos: " + e.getMessage(), e);
        }
    }

    private Medicamento mapRow(ResultSet rs) throws SQLException {
        Medicamento m = new Medicamento();
        m.setId(rs.getLong("id"));
        m.setNombre(rs.getString("nombre"));
        m.setVia(rs.getString("via"));
        m.setUnidadDosis(rs.getString("unidad_dosis"));
        m.setNotas(rs.getString("notas"));
        return m;
    }
}
