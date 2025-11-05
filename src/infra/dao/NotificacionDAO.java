package infra.dao;

import domain.Notificacion;
import java.util.List;

public interface NotificacionDAO {
    Long crear(Notificacion n);
    void marcarEntregada(Long id);
    List<Notificacion> findByRecordatorio(Long recordatorioId);
}
