package infra.dao;

import domain.PacienteMedicamento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PacienteMedicamentoDAO {
    void save(PacienteMedicamento pm); // Alias para savePauta
    void savePauta(PacienteMedicamento pm);
    Optional<PacienteMedicamento> findById(Long pacienteId, Long medicamentoId);
    PacienteMedicamento findByPacienteYMedicamento(Long pacienteId, Long medicamentoId);
    List<PacienteMedicamento> findByPacienteId(Long pacienteId);
    List<PacienteMedicamento> findActivosByPaciente(Long pacienteId);
    boolean updateProximaToma(Long pacienteId, Long medicamentoId, LocalDateTime nuevaHora);
    void marcarInactivo(Long pacienteId, Long medicamentoId);
}
