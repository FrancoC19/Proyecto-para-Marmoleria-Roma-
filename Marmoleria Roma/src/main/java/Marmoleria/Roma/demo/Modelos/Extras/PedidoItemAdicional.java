package Marmoleria.Roma.demo.Modelos.Extras;

import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PedidoItemAdicional {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pedido_item")
    @SequenceGenerator(name = "id_pedido_item", sequenceName = "id_pedido_item", allocationSize = 1)
    private long id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedidos pedido;

    // Null si es un caso puntual no catalogado
    @ManyToOne
    @JoinColumn(name = "id_item_adicional", nullable = true)
    private ItemAdicional itemAdicional;

    // Solo se usa si itemAdicional es null (caso puntual / personalizado)
    private String descripcionManual;

    @DecimalMin(value = "0.0", message = "El precio manual no puede ser negativo")
    private Float precioManual;

    @Enumerated(EnumType.STRING)
    private Moneda monedaManual;

    // Representa metros lineales, metros cuadrados o cantidad de unidades,
    // según el tipoCalculo del itemAdicional. No aplica si es FIJO.
    @DecimalMin(value = "0.0", message = "La cantidad no puede ser negativa")
    private Float cantidad;

    public PedidoItemAdicional() {}

    public PedidoItemAdicional(Pedidos pedido, ItemAdicional itemAdicional, Float cantidad) {
        this.pedido = pedido;
        this.itemAdicional = itemAdicional;
        this.cantidad = cantidad;
    }

    // Constructor para caso puntual/manual
    public PedidoItemAdicional(Pedidos pedido, String descripcionManual, Float precioManual, Moneda monedaManual) {
        this.pedido = pedido;
        this.descripcionManual = descripcionManual;
        this.precioManual = precioManual;
        this.monedaManual = monedaManual;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Pedidos getPedido() { return pedido; }
    public void setPedido(Pedidos pedido) { this.pedido = pedido; }

    public ItemAdicional getItemAdicional() { return itemAdicional; }
    public void setItemAdicional(ItemAdicional itemAdicional) { this.itemAdicional = itemAdicional; }

    public String getDescripcionManual() { return descripcionManual; }
    public void setDescripcionManual(String descripcionManual) { this.descripcionManual = descripcionManual; }

    public Float getPrecioManual() { return precioManual; }
    public void setPrecioManual(Float precioManual) { this.precioManual = precioManual; }

    public Moneda getMonedaManual() { return monedaManual; }
    public void setMonedaManual(Moneda monedaManual) { this.monedaManual = monedaManual; }

    public Float getCantidad() { return cantidad; }
    public void setCantidad(Float cantidad) { this.cantidad = cantidad; }
}
