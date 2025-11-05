package infra.dao;

import domain.Adherencia;
import java.time.LocalDateTime;
import java.util.List;

public interface AdherenciaDAO {
    Long registrarAccion(Adherencia a);
    List<Adherencia> findAll();
    List<Adherencia> findByRecordatorioId(Long recordatorioId);
    List<Adherencia> findByPacienteRangoFechas(Long pacienteId, LocalDateTime desde, LocalDateTime hasta);
}
