package infra.dao;

import domain.Cuidador;
import java.util.List;

public interface CuidadorDAO {
    Long save(Cuidador c);
    void update(Cuidador c);
    void delete(Long id);
    Cuidador findById(Long id);
    List<Cuidador> findAll();
}
