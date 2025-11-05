package domain;

public class Medicamento {
    private Long id;
    private String nombre;
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
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getUnidadDosis() { return unidadDosis; }
    public void setUnidadDosis(String unidadDosis) { this.unidadDosis = unidadDosis; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
