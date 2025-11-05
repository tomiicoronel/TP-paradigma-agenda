package infra.dao.impl;

import domain.Notificacion;
import infra.dao.NotificacionDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAOImpl implements NotificacionDAO {

    @Override
    public Long crear(Notificacion n) {
        String sql = "INSERT INTO notificacion (recordatorio_id, emitida_at, canal_visual, canal_sonoro, entregada) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, n.getRecordatorioId());
            ps.setTimestamp(2, Timestamp.valueOf(n.getEmitidaAt()));
            ps.setString(3, n.getCanalVisual());
            ps.setString(4, n.getCanalSonoro());
            ps.setBoolean(5, n.isEntregada());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new RuntimeException("No se generó ID para notificación");

        } catch (SQLException e) {
            throw new RuntimeException("Error creando notificación: " + e.getMessage(), e);
        }
    }

    @Override
    public void marcarEntregada(Long id) {
        String sql = "UPDATE notificacion SET entregada = TRUE WHERE id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error marcando notificación como entregada: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Notificacion> findByRecordatorio(Long recordatorioId) {
        String sql = "SELECT id, recordatorio_id, emitida_at, canal_visual, canal_sonoro, entregada " +
                     "FROM notificacion WHERE recordatorio_id = ? ORDER BY emitida_at";

        List<Notificacion> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, recordatorioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando notificaciones: " + e.getMessage(), e);
        }
    }

    private Notificacion mapRow(ResultSet rs) throws SQLException {
        Notificacion n = new Notificacion();
        n.setId(rs.getLong("id"));
        n.setRecordatorioId(rs.getLong("recordatorio_id"));
        n.setEmitidaAt(rs.getTimestamp("emitida_at").toLocalDateTime());
        n.setCanalVisual(rs.getString("canal_visual"));
        n.setCanalSonoro(rs.getString("canal_sonoro"));
        n.setEntregada(rs.getBoolean("entregada"));
        return n;
    }
}

