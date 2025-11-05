package infra.dao;

import domain.PacienteMedicamento;
import java.time.LocalDateTime;
import java.util.List;

public interface PacienteMedicamentoDAO {
    void savePauta(PacienteMedicamento pm);
    PacienteMedicamento findByPacienteYMedicamento(Long pacienteId, Long medicamentoId);
    List<PacienteMedicamento> findActivosByPaciente(Long pacienteId);
    boolean updateProximaToma(Long pacienteId, Long medicamentoId, LocalDateTime nuevaHora);
    void marcarInactivo(Long pacienteId, Long medicamentoId);
}
