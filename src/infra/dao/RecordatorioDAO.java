package infra.dao;

import domain.Recordatorio;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordatorioDAO {
    Long crearPendiente(Recordatorio r);
    void update(Recordatorio r);
    void pasarAplazado(Long id, String motivo);
    void pasarPerdido(Long id, String motivo);
    void marcarHecho(Long id);
    Optional<Recordatorio> findById(Long id);
    List<Recordatorio> findAll();
    List<Recordatorio> findByEstado(String estado);
    List<Recordatorio> findByPacienteId(Long pacienteId);
    List<Recordatorio> findPendientesByPacienteYFecha(Long pacienteId, LocalDateTime desde, LocalDateTime hasta);
}
