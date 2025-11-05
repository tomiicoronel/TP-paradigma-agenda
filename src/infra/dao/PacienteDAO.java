package infra.dao;

import domain.Paciente;
import java.util.List;

public interface PacienteDAO {
    Long save(Paciente p);
    void update(Paciente p);
    void delete(Long id);
    Paciente findById(Long id);
    List<Paciente> findByCuidador(Long cuidadorId);
}
