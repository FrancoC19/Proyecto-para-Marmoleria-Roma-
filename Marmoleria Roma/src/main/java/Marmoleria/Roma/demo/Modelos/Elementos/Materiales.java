package Marmoleria.Roma.demo.Modelos.Elementos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Materiales {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_material")
    @SequenceGenerator(name = "id_material", sequenceName = "id_material", allocationSize = 1)
    private long id_materiales;

    @NotBlank(message = "El material debe poseer un nombre...")
    private String nombreMaterial;

    @DecimalMin(value = "0.1", message = "El precio no puede ser cero o negativo")
    private Float valorMetroCuadrado;

    @NotNull(message = "El material debe poseer un tipo...")
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipoMaterial;

    public Materiales() {}

    public Materiales(String nombreMaterial, TipoMaterial tipoMaterial, Float valorMetroCuadrado) {
        this.nombreMaterial = nombreMaterial;
        this.tipoMaterial = tipoMaterial;
        this.valorMetroCuadrado = valorMetroCuadrado;
    }

    public long getId() {
        return id_materiales;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public Float getValorMetroCuadrado() {
        return valorMetroCuadrado;
    }

    public void setValorMetroCuadrado(Float valorMetroCuadrado) {
        this.valorMetroCuadrado = valorMetroCuadrado;
    }

    public TipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(TipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
}
