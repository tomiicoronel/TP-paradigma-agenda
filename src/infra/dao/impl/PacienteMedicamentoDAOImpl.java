package infra.dao.impl;

import domain.PacienteMedicamento;
import infra.dao.PacienteMedicamentoDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteMedicamentoDAOImpl implements PacienteMedicamentoDAO {

    @Override
    public void save(PacienteMedicamento pm) {
        savePauta(pm);
    }

    @Override
    public void savePauta(PacienteMedicamento pm) {
        String sql = "INSERT INTO paciente_medicamento (paciente_id, medicamento_id, dosis, unidad, " +
                     "intervalo_min, ventana_min, hora_inicio, proxima_toma_at, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, pm.getPacienteId());
            ps.setLong(2, pm.getMedicamentoId());
            ps.setDouble(3, pm.getDosis());
            ps.setString(4, pm.getUnidad());
            ps.setInt(5, pm.getIntervaloMin());
            ps.setInt(6, pm.getVentanaMin());
            ps.setTimestamp(7, pm.getHoraInicio() != null ? Timestamp.valueOf(pm.getHoraInicio()) : null);
            ps.setTimestamp(8, pm.getProximaTomaAt() != null ? Timestamp.valueOf(pm.getProximaTomaAt()) : null);
            ps.setBoolean(9, pm.isActivo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando pauta paciente-medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PacienteMedicamento> findById(Long pacienteId, Long medicamentoId) {
        PacienteMedicamento pm = findByPacienteYMedicamento(pacienteId, medicamentoId);
        return Optional.ofNullable(pm);
    }

    @Override
    public PacienteMedicamento findByPacienteYMedicamento(Long pacienteId, Long medicamentoId) {
        String sql = "SELECT paciente_id, medicamento_id, dosis, unidad, intervalo_min, ventana_min, " +
                     "hora_inicio, proxima_toma_at, activo " +
                     "FROM paciente_medicamento WHERE paciente_id = ? AND medicamento_id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, pacienteId);
            ps.setLong(2, medicamentoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando pauta: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PacienteMedicamento> findByPacienteId(Long pacienteId) {
        String sql = "SELECT paciente_id, medicamento_id, dosis, unidad, intervalo_min, ventana_min, " +
                     "hora_inicio, proxima_toma_at, activo " +
                     "FROM paciente_medicamento WHERE paciente_id = ? " +
                     "ORDER BY proxima_toma_at";

        List<PacienteMedicamento> resultado = new ArrayList<>();

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
            throw new RuntimeException("Error listando pautas de paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PacienteMedicamento> findActivosByPaciente(Long pacienteId) {
        String sql = "SELECT paciente_id, medicamento_id, dosis, unidad, intervalo_min, ventana_min, " +
                     "hora_inicio, proxima_toma_at, activo " +
                     "FROM paciente_medicamento WHERE paciente_id = ? AND activo = TRUE " +
                     "ORDER BY proxima_toma_at";

        List<PacienteMedicamento> resultado = new ArrayList<>();

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
            throw new RuntimeException("Error listando pautas activas: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateProximaToma(Long pacienteId, Long medicamentoId, LocalDateTime nuevaHora) {
        String sql = "UPDATE paciente_medicamento SET proxima_toma_at = ? " +
                     "WHERE paciente_id = ? AND medicamento_id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, nuevaHora != null ? Timestamp.valueOf(nuevaHora) : null);
            ps.setLong(2, pacienteId);
            ps.setLong(3, medicamentoId);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando próxima toma: " + e.getMessage(), e);
        }
    }

    @Override
    public void marcarInactivo(Long pacienteId, Long medicamentoId) {
        String sql = "UPDATE paciente_medicamento SET activo = FALSE " +
                     "WHERE paciente_id = ? AND medicamento_id = ?";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, pacienteId);
            ps.setLong(2, medicamentoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error marcando pauta como inactiva: " + e.getMessage(), e);
        }
    }

    private PacienteMedicamento mapRow(ResultSet rs) throws SQLException {
        PacienteMedicamento pm = new PacienteMedicamento();
        pm.setPacienteId(rs.getLong("paciente_id"));
        pm.setMedicamentoId(rs.getLong("medicamento_id"));
        pm.setDosis(rs.getDouble("dosis"));
        pm.setUnidad(rs.getString("unidad"));
        pm.setIntervaloMin(rs.getInt("intervalo_min"));
        pm.setVentanaMin(rs.getInt("ventana_min"));

        Timestamp horaInicio = rs.getTimestamp("hora_inicio");
        pm.setHoraInicio(horaInicio != null ? horaInicio.toLocalDateTime() : null);

        Timestamp proximaToma = rs.getTimestamp("proxima_toma_at");
        pm.setProximaTomaAt(proximaToma != null ? proximaToma.toLocalDateTime() : null);

        pm.setActivo(rs.getBoolean("activo"));
        return pm;
    }
}
