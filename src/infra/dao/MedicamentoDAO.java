package infra.dao;

import domain.Medicamento;
import java.util.List;
import java.util.Optional;

public interface MedicamentoDAO {
    Long save(Medicamento m);
    void update(Medicamento m);
    void delete(Long id);
    Optional<Medicamento> findById(Long id);
    List<Medicamento> searchByNombre(String nombre);
    List<Medicamento> findAll();
}
