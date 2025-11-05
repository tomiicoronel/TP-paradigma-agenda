package infra.dao;

import domain.Medicamento;
import java.util.List;

public interface MedicamentoDAO {
    Long save(Medicamento m);
    void update(Medicamento m);
    void delete(Long id);
    Medicamento findById(Long id);
    List<Medicamento> searchByNombre(String nombre);
}
