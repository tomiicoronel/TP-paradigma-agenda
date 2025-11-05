package infra.dao;

import domain.Cuidador;
import java.util.List;
import java.util.Optional;

public interface CuidadorDAO {
    Long save(Cuidador c);
    void update(Cuidador c);
    void delete(Long id);
    Optional<Cuidador> findById(Long id);
    List<Cuidador> findAll();
}
