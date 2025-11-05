package domain;

import java.time.LocalDateTime;

public class Recordatorio {
    private Long id;
    private Long pacienteId;
    private Long medicamentoId; // Para acceso rápido en queries
    private String tipo; // MEDICACION | ACTIVIDAD | TURNO
    private String referenciaTipo; // PAC_MED | ITEM_RUTINA
    private Long referenciaId;
    private LocalDateTime programadoAt;
    private LocalDateTime realizadoAt; // Cuando se marcó como HECHO
    private int ventanaMin;
    private String estado; // PENDIENTE | APLAZADO | PERDIDO | HECHO
    private String motivoEstado;

    public Recordatorio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getReferenciaTipo() { return referenciaTipo; }
    public void setReferenciaTipo(String referenciaTipo) { this.referenciaTipo = referenciaTipo; }

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public LocalDateTime getProgramadoAt() { return programadoAt; }
    public void setProgramadoAt(LocalDateTime programadoAt) { this.programadoAt = programadoAt; }

    public LocalDateTime getRealizadoAt() { return realizadoAt; }
    public void setRealizadoAt(LocalDateTime realizadoAt) { this.realizadoAt = realizadoAt; }

    public int getVentanaMin() { return ventanaMin; }
    public void setVentanaMin(int ventanaMin) { this.ventanaMin = ventanaMin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoEstado() { return motivoEstado; }
    public void setMotivoEstado(String motivoEstado) { this.motivoEstado = motivoEstado; }
}
