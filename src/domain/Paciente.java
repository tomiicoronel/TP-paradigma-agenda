package domain;

import java.time.LocalDate;

public class Paciente {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String diagnostico;
    private String preferenciasAccesibilidad;
    private Long cuidadorId;

    public Paciente() {}

    public Paciente(Long id, String nombre, String preferenciasAccesibilidad, Long cuidadorId) {
        this.id = id;
        this.nombre = nombre;
        this.preferenciasAccesibilidad = preferenciasAccesibilidad;
        this.cuidadorId = cuidadorId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getPreferenciasAccesibilidad() { return preferenciasAccesibilidad; }
    public void setPreferenciasAccesibilidad(String preferenciasAccesibilidad) {
        this.preferenciasAccesibilidad = preferenciasAccesibilidad;
    }

    public Long getCuidadorId() { return cuidadorId; }
    public void setCuidadorId(Long cuidadorId) { this.cuidadorId = cuidadorId; }

    /**
     * Calcula la edad del paciente basado en su fecha de nacimiento.
     * @return edad en años, o null si no tiene fecha de nacimiento
     */
    public Integer getEdad() {
        if (fechaNacimiento == null) {
            return null;
        }
        return java.time.Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
