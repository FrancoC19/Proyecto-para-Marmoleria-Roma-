package Marmoleria.Roma.demo.Modelos.Extras;

import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GrupoPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_grupo_precio")
    @SequenceGenerator(name = "id_grupo_precio", sequenceName = "id_grupo_precio", allocationSize = 1)
    private long id;

    @Version
    private Long version;

    @NotNull(message = "El grupo debe tener un tipo de material asociado")
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipoMaterial;

    @NotBlank(message = "El grupo debe tener un nombre")
    private String nombre; // "Nacional", "Importado", "Grupo 0", "Grupo 3", etc.

    // Solo obligatorio/validado cuando precioCompartido es true (ver ServiceGrupoPrecio).
    // En modo individual cada Materiales tiene su propio precioPorM2Individual y este campo no se usa.
    private Float precioPorM2;

    private boolean activo = true;

    @OneToMany(mappedBy = "grupoPrecio")
    @JsonIgnore
    private List<Materiales> materiales;

    @NotNull(message = "El grupo debe tener una moneda")
    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @NotNull(message = "Debe indicarse si el precio es compartido o individual")
    private boolean precioCompartido;

    public GrupoPrecio() {}

    public GrupoPrecio(TipoMaterial tipoMaterial, String nombre, Float precioPorM2, Moneda moneda, boolean precioCompartido) {
        this.tipoMaterial = tipoMaterial;
        this.nombre = nombre;
        this.precioPorM2 = precioPorM2;
        this.moneda = moneda;
        this.precioCompartido = precioCompartido;
        this.activo = true;
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public TipoMaterial getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(TipoMaterial tipoMaterial) { this.tipoMaterial = tipoMaterial; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Float getPrecioPorM2() { return precioPorM2; }
    public void setPrecioPorM2(Float precioPorM2) { this.precioPorM2 = precioPorM2; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Moneda getMoneda() { return moneda; }
    public void setMoneda(Moneda moneda) { this.moneda = moneda; }

    @NotNull(message = "Debe indicarse si el precio es compartido o individual")
    public boolean isPrecioCompartido() {
        return precioCompartido;
    }

    public void setPrecioCompartido(@NotNull(message = "Debe indicarse si el precio es compartido o individual") boolean precioCompartido) {
        this.precioCompartido = precioCompartido;
    }
}
