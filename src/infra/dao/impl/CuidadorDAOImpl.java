package infra.dao.impl;

import domain.Cuidador;
import infra.dao.CuidadorDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuidadorDAOImpl implements CuidadorDAO {

    @Override
    public Long save(Cuidador c) {
        String sql = "INSERT INTO cuidador (nombre, contacto) VALUES (?, ?)";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getContacto());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    c.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se generó ID para cuidador");
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando cuidador: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Cuidador c) {
        String sql = "UPDATE cuidador SET nombre = ?, contacto = ? WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getContacto());
            ps.setLong(3, c.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando cuidador: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cuidador WHERE id = ?";
        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando cuidador: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cuidador> findById(Long id) {
        String sql = "SELECT id, nombre, contacto FROM cuidador WHERE id = ?";
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
            throw new RuntimeException("Error buscando cuidador: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cuidador> findAll() {
        String sql = "SELECT id, nombre, contacto FROM cuidador ORDER BY nombre";
        List<Cuidador> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapRow(rs));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando cuidadores: " + e.getMessage(), e);
        }
    }

    private Cuidador mapRow(ResultSet rs) throws SQLException {
        Cuidador c = new Cuidador();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setContacto(rs.getString("contacto"));
        return c;
    }
}
