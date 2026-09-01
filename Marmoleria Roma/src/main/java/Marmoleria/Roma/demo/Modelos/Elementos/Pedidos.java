package Marmoleria.Roma.demo.Modelos.Elementos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Extras.PedidoItemAdicional;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pedido")
    @SequenceGenerator(name = "id_pedido", sequenceName = "id_pedido", allocationSize = 1)
    private Long idPedido;

    @Version
    private Long version;

    // Texto libre opcional con observaciones sobre el pedido
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "El pedido debe tener un cliente")
    private Cliente cliente;

    // Se asigna recién al finalizar el proceso (EN_PROCESO -> PENDIENTE_DE_ENTREGA), no al cargar el pedido.
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = true)
    private Empleado empleado;

    @Min(value = 0, message = "La seña no puede ser menor a 0")
    private Integer senia;


    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    @NotNull(message = "El pedido debe tener un material")
    private Materiales material;

    @ManyToOne
    @JoinColumn(name = "id_pileta", nullable = true)
    private Piletas pileta;

    // Solo tiene sentido cuando pileta es null: true = el cliente trae su propia pileta,
    // false = no hay pileta en el pedido (mesada ciega).
    private boolean piletaDeCliente = false;

    @NotBlank(message = "El pedido debe tener una grifería")
    private String griferia;

    private String moldura;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "Debe especificarse el total de metros cuadrados")
    private Float metrosCuadrados;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pedido")
    private List<PedidoItemAdicional> itemsAdicionales = new ArrayList<>();

    @Embedded
    private Direccion direccion;

    protected String estado= EstadoPedido.EN_PROCESO.toString();

    private Float valorTotal;
    private Float descuento;

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Pedidos() {}

    // Constructor de conveniencia
    public Pedidos(
            Cliente cliente,Empleado empleado, Float descuento, LocalDate fechaEmision,
            LocalDate fechaEntrega, String griferia, Materiales material,
            Float metrosCuadrados, String moldura, String observaciones,
            Piletas pileta, Integer senia, Direccion direccion) {

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
        this.direccion = direccion;
        this.empleado = empleado;

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

    public boolean isPiletaDeCliente() {
        return piletaDeCliente;
    }

    public void setPiletaDeCliente(boolean piletaDeCliente) {
        this.piletaDeCliente = piletaDeCliente;
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

    public List<PedidoItemAdicional> getItemsAdicionales() {
        return itemsAdicionales;
    }

    public void setItemsAdicionales(List<PedidoItemAdicional> itemsAdicionales) {
        this.itemsAdicionales = itemsAdicionales;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setValorTotalCalculado(Float valorTotal) {
        this.valorTotal = valorTotal;
    }
}