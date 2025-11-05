package controller;

/*
TomaService:
- Genera/actualiza Recordatorios según proxima_toma_at y ventana_min.
- Cambia estados: PENDIENTE -> APLAZADO -> PERDIDO -> HECHO.
- Recalcula proxima_toma_at al marcar HECHO (hora_real + intervalo_min).
- Exponer tick() para pruebas manuales.
*/

public class TomaService {
    public void start() {
        // TODO: programar ScheduledExecutorService que invoque tick() cada X segundos
    }

    public void tick() {
        // TODO: consultar DAOs; crear/actualizar recordatorios y notificaciones
    }

    public void marcarHecho(long recordatorioId) {
        // TODO: cerrar recordatorio y recalcular proxima_toma_at
    }

    public void posponer(long recordatorioId, int minutos) {
        // TODO: ajustar programado_at dentro de límites seguros
    }
}
