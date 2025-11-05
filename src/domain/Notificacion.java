package domain;

import java.time.LocalDateTime;

public class Notificacion {
    private Long id;
    private Long recordatorioId;
    private LocalDateTime emitidaAt;
    private String canalVisual;
    private String canalSonoro;
    private boolean entregada;

    public Notificacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecordatorioId() { return recordatorioId; }
    public void setRecordatorioId(Long recordatorioId) { this.recordatorioId = recordatorioId; }

    public LocalDateTime getEmitidaAt() { return emitidaAt; }
    public void setEmitidaAt(LocalDateTime emitidaAt) { this.emitidaAt = emitidaAt; }

    public String getCanalVisual() { return canalVisual; }
    public void setCanalVisual(String canalVisual) { this.canalVisual = canalVisual; }

    public String getCanalSonoro() { return canalSonoro; }
    public void setCanalSonoro(String canalSonoro) { this.canalSonoro = canalSonoro; }

    public boolean isEntregada() { return entregada; }
    public void setEntregada(boolean entregada) { this.entregada = entregada; }
}
