package Marmoleria.Roma.demo.Modelos.Elementos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Materiales {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_material")
    @SequenceGenerator(name = "id_material", sequenceName = "id_material", allocationSize = 1)
    @JsonProperty("id")
    private long id_materiales;

    @Version
    private Long version;

    @NotBlank(message = "El material debe poseer un nombre...")
    private String nombreMaterial;

    @NotNull(message = "El material debe poseer un tipo...")
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipoMaterial;

    @NotNull(message = "El material debe poseer un grupo de precio...")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_precio_id")
    private GrupoPrecio grupoPrecio;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio individual debe ser mayor a cero")
    private Float precioPorM2Individual;

    @OneToMany(mappedBy = "material", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Pedidos> pedidos;

    public Materiales() {}

    public Materiales(String nombreMaterial, TipoMaterial tipoMaterial, GrupoPrecio grupoPrecio) {
        this.nombreMaterial = nombreMaterial;
        this.tipoMaterial = tipoMaterial;
        this.grupoPrecio = grupoPrecio;
    }

    public Long getId() { return id_materiales; }
    public void setId(long id_materiales) { this.id_materiales = id_materiales; }

    public String getNombreMaterial() { return nombreMaterial; }
    public void setNombreMaterial(String nombreMaterial) { this.nombreMaterial = nombreMaterial; }

    public TipoMaterial getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(TipoMaterial tipoMaterial) { this.tipoMaterial = tipoMaterial; }

    public GrupoPrecio getGrupoPrecio() { return grupoPrecio; }
    public void setGrupoPrecio(GrupoPrecio grupoPrecio) { this.grupoPrecio = grupoPrecio; }

    public Float getPrecioPorM2Individual() {
        return precioPorM2Individual;
    }

    public void setPrecioPorM2Individual(Float precioPorM2Individual) {
        this.precioPorM2Individual = precioPorM2Individual;
    }

    // Se mantiene por compatibilidad con el resto del sistema (PDF, pedidos, etc.)
    public Float getValorMetroCuadrado() {
        if (grupoPrecio == null) return null;
        return grupoPrecio.isPrecioCompartido()
                ? grupoPrecio.getPrecioPorM2()
                : this.precioPorM2Individual;
    }
}
