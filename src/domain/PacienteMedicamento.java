package domain;

import java.time.LocalDateTime;

public class PacienteMedicamento {
    private Long pacienteId;
    private Long medicamentoId;
    private double dosis;
    private String unidad;
    private int intervaloMin;
    private int ventanaMin;
    private LocalDateTime horaInicio;
    private LocalDateTime proximaTomaAt;
    private boolean activo;

    public PacienteMedicamento() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }

    public double getDosis() { return dosis; }
    public void setDosis(double dosis) { this.dosis = dosis; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public int getIntervaloMin() { return intervaloMin; }
    public void setIntervaloMin(int intervaloMin) { this.intervaloMin = intervaloMin; }

    public int getVentanaMin() { return ventanaMin; }
    public void setVentanaMin(int ventanaMin) { this.ventanaMin = ventanaMin; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getProximaTomaAt() { return proximaTomaAt; }
    public void setProximaTomaAt(LocalDateTime proximaTomaAt) { this.proximaTomaAt = proximaTomaAt; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
