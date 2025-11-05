package domain;

public class Paciente {
    private Long id;
    private String nombre;
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

    public String getPreferenciasAccesibilidad() { return preferenciasAccesibilidad; }
    public void setPreferenciasAccesibilidad(String preferenciasAccesibilidad) {
        this.preferenciasAccesibilidad = preferenciasAccesibilidad;
    }

    public Long getCuidadorId() { return cuidadorId; }
    public void setCuidadorId(Long cuidadorId) { this.cuidadorId = cuidadorId; }
}
