package infra.dao.impl;

import domain.Adherencia;
import infra.dao.AdherenciaDAO;
import infra.db.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdherenciaDAOImpl implements AdherenciaDAO {

    @Override
    public Long registrarAccion(Adherencia a) {
        String sql = "INSERT INTO adherencia (recordatorio_id, registrada_at, accion, observaciones) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, a.getRecordatorioId());
            ps.setTimestamp(2, Timestamp.valueOf(a.getRegistradaAt()));
            ps.setString(3, a.getAccion());
            ps.setString(4, a.getObservaciones());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    a.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se generó ID para adherencia");

        } catch (SQLException e) {
            throw new RuntimeException("Error registrando adherencia: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Adherencia> findAll() {
        String sql = "SELECT id, recordatorio_id, registrada_at, accion, observaciones " +
                     "FROM adherencia ORDER BY registrada_at DESC";

        List<Adherencia> resultado = new ArrayList<>();

        try (Connection cn = ConexionDB.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapRow(rs));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando adherencias: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Adherencia> findByRecordatorioId(Long recordatorioId) {
        String sql = "SELECT id, recordatorio_id, registrada_at, accion, observaciones " +
                     "FROM adherencia WHERE recordatorio_id = ? ORDER BY registrada_at";

        List<Adherencia> resultado = new ArrayList<>();

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
            throw new RuntimeException("Error buscando adherencias por recordatorio: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Adherencia> findByPacienteRangoFechas(Long pacienteId, LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT a.id, a.recordatorio_id, a.registrada_at, a.accion, a.observaciones " +
                     "FROM adherencia a " +
                     "JOIN recordatorio r ON a.recordatorio_id = r.id " +
                     "WHERE r.paciente_id = ? AND a.registrada_at BETWEEN ? AND ? " +
                     "ORDER BY a.registrada_at DESC";

        List<Adherencia> resultado = new ArrayList<>();

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
            throw new RuntimeException("Error buscando adherencia por paciente: " + e.getMessage(), e);
        }
    }

    private Adherencia mapRow(ResultSet rs) throws SQLException {
        Adherencia a = new Adherencia();
        a.setId(rs.getLong("id"));
        a.setRecordatorioId(rs.getLong("recordatorio_id"));
        a.setRegistradaAt(rs.getTimestamp("registrada_at").toLocalDateTime());
        a.setAccion(rs.getString("accion"));
        a.setObservaciones(rs.getString("observaciones"));
        return a;
    }
}
