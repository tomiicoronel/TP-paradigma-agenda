package domain;

import java.time.LocalDateTime;

public class Adherencia {
    private Long id;
    private Long recordatorioId;
    private LocalDateTime registradaAt;
    private String estadoPrevio; // Estado anterior del recordatorio
    private String estadoNuevo; // Nuevo estado del recordatorio
    private String accion; // HECHO | OMITIDO | POSPUESTO
    private String observaciones;

    public Adherencia() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecordatorioId() { return recordatorioId; }
    public void setRecordatorioId(Long recordatorioId) { this.recordatorioId = recordatorioId; }

    public LocalDateTime getRegistradaAt() { return registradaAt; }
    public void setRegistradaAt(LocalDateTime registradaAt) { this.registradaAt = registradaAt; }

    // Alias para compatibilidad
    public LocalDateTime getRegistradoAt() { return registradaAt; }
    public void setRegistradoAt(LocalDateTime registradoAt) { this.registradaAt = registradoAt; }

    public String getEstadoPrevio() { return estadoPrevio; }
    public void setEstadoPrevio(String estadoPrevio) { this.estadoPrevio = estadoPrevio; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
