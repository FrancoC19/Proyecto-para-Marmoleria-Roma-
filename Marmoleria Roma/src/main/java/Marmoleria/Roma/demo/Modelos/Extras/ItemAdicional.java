package Marmoleria.Roma.demo.Modelos.Extras;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoCalculo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemAdicional {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_item_adicional")
    @SequenceGenerator(name = "id_item_adicional", sequenceName = "id_item_adicional", allocationSize = 1)
    private long id;

    @Version
    private Long version;

    @NotBlank(message = "El ítem debe tener un nombre")
    private String nombre;

    @NotNull(message = "El ítem debe tener un tipo de cálculo")
    @Enumerated(EnumType.STRING)
    private TipoCalculo tipoCalculo;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero")
    private Float precio;

    @NotNull(message = "El ítem debe tener una moneda")
    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    private boolean activo = true;

    public ItemAdicional() {}

    public ItemAdicional(String nombre, TipoCalculo tipoCalculo, Float precio, Moneda moneda) {
        this.nombre = nombre;
        this.tipoCalculo = tipoCalculo;
        this.precio = precio;
        this.moneda = moneda;
        this.activo = true;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoCalculo getTipoCalculo() { return tipoCalculo; }
    public void setTipoCalculo(TipoCalculo tipoCalculo) { this.tipoCalculo = tipoCalculo; }

    public Float getPrecio() { return precio; }
    public void setPrecio(Float precio) { this.precio = precio; }

    public Moneda getMoneda() { return moneda; }
    public void setMoneda(Moneda moneda) { this.moneda = moneda; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

