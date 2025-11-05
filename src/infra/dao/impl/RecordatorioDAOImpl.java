package infra.dao.impl;

import domain.Recordatorio;
import infra.dao.RecordatorioDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDAOImpl implements RecordatorioDAO {

    @Override
    public Long crearPendiente(Recordatorio r) {
        String sql = "INSERT INTO recordatorio (paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'PENDIENTE', ?)";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, r.getPacienteId());
            ps.setString(2, r.getTipo());
            ps.setString(3, r.getReferenciaTipo());
            ps.setLong(4, r.getReferenciaId());
            ps.setTimestamp(5, Timestamp.valueOf(r.getProgramadoAt()));
            ps.setInt(6, r.getVentanaMin());
            ps.setString(7, r.getMotivoEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new RuntimeException("No se generó ID para recordatorio");

        } catch (SQLException e) {
            throw new RuntimeException("Error creando recordatorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void pasarAplazado(Long id, String motivo) {
        actualizarEstado(id, "APLAZADO", motivo);
    }

    @Override
    public void pasarPerdido(Long id, String motivo) {
        actualizarEstado(id, "PERDIDO", motivo);
    }

    @Override
    public void marcarHecho(Long id) {
        actualizarEstado(id, "HECHO", null);
    }

    @Override
    public List<Recordatorio> findPendientesByPacienteYFecha(Long pacienteId, LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT id, paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado " +
                     "FROM recordatorio " +
                     "WHERE paciente_id = ? AND estado = 'PENDIENTE' " +
                     "AND programado_at BETWEEN ? AND ? " +
                     "ORDER BY programado_at";

        List<Recordatorio> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, pacienteId);
            ps.setTimestamp(2, Timestamp.valueOf(desde));
            ps.setTimestamp(3, Timestamp.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando recordatorios pendientes: " + e.getMessage(), e);
        }
    }

    private void actualizarEstado(Long id, String estado, String motivo) {
        String sql = "UPDATE recordatorio SET estado = ?, motivo_estado = ? WHERE id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setString(2, motivo);
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando estado de recordatorio: " + e.getMessage(), e);
        }
    }

    private Recordatorio mapRow(ResultSet rs) throws SQLException {
        Recordatorio r = new Recordatorio();
        r.setId(rs.getLong("id"));
        r.setPacienteId(rs.getLong("paciente_id"));
        r.setTipo(rs.getString("tipo"));
        r.setReferenciaTipo(rs.getString("referencia_tipo"));
        r.setReferenciaId(rs.getLong("referencia_id"));
        r.setProgramadoAt(rs.getTimestamp("programado_at").toLocalDateTime());
        r.setVentanaMin(rs.getInt("ventana_min"));
        r.setEstado(rs.getString("estado"));
        r.setMotivoEstado(rs.getString("motivo_estado"));
        return r;
    }
}

