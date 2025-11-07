package Marmoleria.Roma.demo.Modelos.Elementos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pedido")
    @SequenceGenerator(name = "id_pedido", sequenceName = "id_pedido", allocationSize = 1)
    private Long idPedido;

    // Texto libre opcional con observaciones sobre el pedido
    private String observaciones;

    /**
     * RELACIÓN con Cliente:
     * Muchos pedidos (Many) pueden pertenecer a un solo cliente (One).
     * En la BD esto genera una columna "id_cliente" como clave foránea.
     * En Java permite acceder al objeto Cliente completo desde un Pedido.
     *
     * ⚠️ Aunque en el JSON de entrada mandes solo { "id": 1 },
     * JPA usará ese id para vincular el cliente existente.
     */
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "El pedido debe tener un cliente")
    private Cliente cliente;

    // 🔹 NUEVO CAMPO: relación con empleado
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    @NotNull(message = "El pedido debe tener un empleado asignado")
    private Empleado empleado;

    @Min(value = 0, message = "La seña no puede ser menor a 0")
    private Integer senia;

    /**
     * RELACIÓN con Materiales:
     * Muchos pedidos pueden usar un mismo material.
     * Genera una columna "id_material" como foreign key en la tabla pedidos.
     */
    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    @NotNull(message = "El pedido debe tener un material")
    private Materiales material;

    /**
     * RELACIÓN con Piletas:
     * Muchos pedidos pueden tener la misma pileta asociada.
     * También crea una foreign key "id_pileta".
     */
    @ManyToOne
    @JoinColumn(name = "id_pileta", nullable = false)
    private Piletas pileta;

    @NotBlank(message = "El pedido debe tener una grifería")
    private String griferia;

    private String moldura;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "Debe especificarse el total de metros cuadrados")
    private Float metrosCuadrados;

    protected String estado= EstadoPedido.EN_PROCESO.toString();

    private Float valorTotal;
    private Float descuento;

    public Pedidos() {}

    // Constructor de conveniencia
    public Pedidos(
            Cliente cliente, Float descuento, LocalDate fechaEmision,
            LocalDate fechaEntrega, String griferia, Materiales material,
            Float metrosCuadrados, String moldura, String observaciones,
            Piletas pileta, Integer senia, Float valorTotal) {

        this.cliente = cliente;
        this.descuento = descuento;
        this.fechaEmision = fechaEmision;
        this.fechaEntrega = fechaEntrega;
        this.griferia = griferia;
        this.material = material;
        this.metrosCuadrados = metrosCuadrados;
        this.moldura = moldura;
        this.observaciones = observaciones;
        this.pileta = pileta;
        this.senia = senia;
        this.valorTotal = valorTotal;
    }

    // --- Getters y Setters ---

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getSenia() {
        return senia;
    }

    public void setSenia(Integer senia) {
        this.senia = senia;
    }

    public Materiales getMaterial() {
        return material;
    }

    public void setMaterial(Materiales material) {
        this.material = material;
    }

    public Piletas getPileta() {
        return pileta;
    }

    public void setPileta(Piletas pileta) {
        this.pileta = pileta;
    }

    public String getGriferia() {
        return griferia;
    }

    public void setGriferia(String griferia) {
        this.griferia = griferia;
    }

    public String getMoldura() {
        return moldura;
    }

    public void setMoldura(String moldura) {
        this.moldura = moldura;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Float getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Float metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Calcula el valor total del pedido según:
     * (metros * valor del material) + valor pileta - descuento (si aplica)
     */
    public float calcularValor() {
        float resultado = (this.metrosCuadrados * this.material.getValorMetroCuadrado())
                + this.pileta.getValor();

        if (this.descuento != null && this.descuento > 0) {
            resultado -= resultado * this.descuento;
        }

        return resultado;
    }
}