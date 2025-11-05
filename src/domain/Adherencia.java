package domain;

import java.time.LocalDateTime;

public class Adherencia {
    private Long id;
    private Long recordatorioId;
    private LocalDateTime registradaAt;
    private String accion; // HECHO | OMITIDO | POSPUESTO
    private String observaciones;

    public Adherencia() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecordatorioId() { return recordatorioId; }
    public void setRecordatorioId(Long recordatorioId) { this.recordatorioId = recordatorioId; }

    public LocalDateTime getRegistradaAt() { return registradaAt; }
    public void setRegistradaAt(LocalDateTime registradaAt) { this.registradaAt = registradaAt; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
