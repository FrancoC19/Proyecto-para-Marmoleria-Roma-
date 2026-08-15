package Marmoleria.Roma.demo.Modelos.Extras;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_pedido", "numero_de_imagen_del_pedido"}))
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_imagen")
    @SequenceGenerator(name = "id_imagen", sequenceName = "id_imagen", allocationSize = 1)
    private Long idImagen;

    @Version
    private Long version;

    private int numeroDeImagenDelPedido=0;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @NotNull(message = "El pedido debe tener un cliente")
    private Pedidos pedido;

    @Lob
    @Column(nullable = false)
    private byte[] imagen;

    public Imagen(Long version, byte[] imagen, Pedidos pedido) {
        this.version = version;
        this.imagen = imagen;
        this.pedido = pedido;
    }

    public Imagen() {

    }

    public Long getVersion() {
        return version;
    }

    public Long getIdImagen() {
        return idImagen;
    }

    public @NotNull(message = "El pedido debe tener un cliente") Pedidos getPedido() {
        return pedido;
    }

    public int getNumeroDeImagenDelPedido() {
        return numeroDeImagenDelPedido;
    }

    public void setNumeroDeImagenDelPedido(int numeroDeImagenDelPedido) {
        this.numeroDeImagenDelPedido = numeroDeImagenDelPedido;
    }

    public void setPedido(@NotNull(message = "El pedido debe tener un cliente") Pedidos pedido) {
        this.pedido = pedido;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imagen imagen)) return false;
        return Objects.equals(idImagen, imagen.idImagen);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idImagen);
    }
}
