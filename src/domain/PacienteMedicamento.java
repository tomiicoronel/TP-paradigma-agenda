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

    // Método para compatibilidad con la CLI que espera String
    public void setDosis(String dosisStr) {
        if (dosisStr != null && !dosisStr.isEmpty()) {
            try {
                this.dosis = Double.parseDouble(dosisStr);
            } catch (NumberFormatException e) {
                // Si no es número, guardamos como texto en unidad
                this.unidad = dosisStr;
            }
        }
    }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public int getIntervaloMin() { return intervaloMin; }
    public void setIntervaloMin(int intervaloMin) { this.intervaloMin = intervaloMin; }

    // Alias para compatibilidad
    public int getIntervaloMinutos() { return intervaloMin; }
    public void setIntervaloMinutos(int intervaloMin) { this.intervaloMin = intervaloMin; }

    public int getVentanaMin() { return ventanaMin; }
    public void setVentanaMin(int ventanaMin) { this.ventanaMin = ventanaMin; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getProximaTomaAt() { return proximaTomaAt; }
    public void setProximaTomaAt(LocalDateTime proximaTomaAt) { this.proximaTomaAt = proximaTomaAt; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Alias para compatibilidad
    public boolean isActiva() { return activo; }
    public void setActiva(boolean activa) { this.activo = activa; }
}
