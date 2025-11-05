package domain;

public class Medicamento {
    private Long id;
    private String nombre;
    private String nombreComercial; // Alias para nombre
    private String droga; // Principio activo
    private String presentacion; // Forma farmacéutica
    private String via;
    private String unidadDosis;
    private String notas;

    public Medicamento() {}

    public Medicamento(Long id, String nombre, String via, String unidadDosis, String notas) {
        this.id = id;
        this.nombre = nombre;
        this.via = via;
        this.unidadDosis = unidadDosis;
        this.notas = notas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreComercial = nombre; // Sincronizar
    }

    public String getNombreComercial() { return nombreComercial != null ? nombreComercial : nombre; }
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
        this.nombre = nombreComercial; // Sincronizar
    }

    public String getDroga() { return droga; }
    public void setDroga(String droga) { this.droga = droga; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getUnidadDosis() { return unidadDosis; }
    public void setUnidadDosis(String unidadDosis) { this.unidadDosis = unidadDosis; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
