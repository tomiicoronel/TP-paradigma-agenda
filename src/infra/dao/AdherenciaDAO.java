package infra.dao;

import domain.Adherencia;
import java.time.LocalDateTime;
import java.util.List;

public interface AdherenciaDAO {
    Long registrarAccion(Adherencia a);
    List<Adherencia> findByPacienteRangoFechas(Long pacienteId, LocalDateTime desde, LocalDateTime hasta);
}
