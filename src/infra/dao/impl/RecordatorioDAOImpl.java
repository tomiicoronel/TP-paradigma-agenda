package infra.dao.impl;

import domain.Recordatorio;
import infra.dao.RecordatorioDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                    Long id = rs.getLong(1);
                    r.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se generó ID para recordatorio");

        } catch (SQLException e) {
            throw new RuntimeException("Error creando recordatorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Recordatorio r) {
        String sql = "UPDATE recordatorio SET estado = ?, motivo_estado = ? WHERE id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, r.getEstado());
            ps.setString(2, r.getMotivoEstado());
            ps.setLong(3, r.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando recordatorio: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Recordatorio> findById(Long id) {
        String sql = "SELECT id, paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado " +
                     "FROM recordatorio WHERE id = ?";

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
            throw new RuntimeException("Error buscando recordatorio: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Recordatorio> findAll() {
        String sql = "SELECT id, paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado " +
                     "FROM recordatorio ORDER BY programado_at DESC";

        List<Recordatorio> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapRow(rs));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando recordatorios: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Recordatorio> findByEstado(String estado) {
        String sql = "SELECT id, paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado " +
                     "FROM recordatorio WHERE estado = ? ORDER BY programado_at";

        List<Recordatorio> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando recordatorios por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Recordatorio> findByPacienteId(Long pacienteId) {
        String sql = "SELECT id, paciente_id, tipo, referencia_tipo, referencia_id, " +
                     "programado_at, ventana_min, estado, motivo_estado " +
                     "FROM recordatorio WHERE paciente_id = ? ORDER BY programado_at DESC";

        List<Recordatorio> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, pacienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapRow(rs));
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando recordatorios por paciente: " + e.getMessage(), e);
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

    @Override
    public List<Recordatorio> findByRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT * FROM recordatorio WHERE programado_at BETWEEN ? AND ? ORDER BY programado_at ASC";
        List<Recordatorio> lista = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando recordatorios por rango: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<Recordatorio> findProximosNMinutos(int minutos) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusMinutes(minutos);

        String sql = "SELECT * FROM recordatorio WHERE programado_at BETWEEN ? AND ? " +
                     "AND estado IN ('PENDIENTE', 'APLAZADO') ORDER BY programado_at ASC";
        List<Recordatorio> lista = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(ahora));
            ps.setTimestamp(2, Timestamp.valueOf(limite));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando próximos recordatorios: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public void actualizarHoraProgramada(Long id, LocalDateTime nuevaHora) {
        String sql = "UPDATE recordatorio SET programado_at = ?, actualizado_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(nuevaHora));
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando hora programada: " + e.getMessage(), e);
        }
    }

    @Override
    public void cambiarEstado(Long id, String nuevoEstado) {
        String sql = "UPDATE recordatorio SET estado = ?, actualizado_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error cambiando estado: " + e.getMessage(), e);
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
