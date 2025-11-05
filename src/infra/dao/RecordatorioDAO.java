package infra.dao;

import domain.Recordatorio;
import java.time.LocalDateTime;
import java.util.List;

public interface RecordatorioDAO {
    Long crearPendiente(Recordatorio r);
    void pasarAplazado(Long id, String motivo);
    void pasarPerdido(Long id, String motivo);
    void marcarHecho(Long id);
    List<Recordatorio> findPendientesByPacienteYFecha(Long pacienteId, LocalDateTime desde, LocalDateTime hasta);
}
