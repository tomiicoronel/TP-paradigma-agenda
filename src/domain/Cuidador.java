package domain;

public class Cuidador {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String contacto; // Campo original del schema
    private String relacion; // Relación con el paciente (ej: familiar, enfermero, etc.)

    public Cuidador() {}

    public Cuidador(Long id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }
}
