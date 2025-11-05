package infra.dao;

import domain.Paciente;
import java.util.List;
import java.util.Optional;

public interface PacienteDAO {
    Long save(Paciente p);
    void update(Paciente p);
    void delete(Long id);
    Optional<Paciente> findById(Long id);
    List<Paciente> findByCuidador(Long cuidadorId);
    List<Paciente> findAll();
}
