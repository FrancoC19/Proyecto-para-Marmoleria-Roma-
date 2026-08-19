package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Extras.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryImagen extends JpaRepository<Imagen, Long> {
    // Todas las imágenes de un pedido
    List<Imagen> findByPedido(Pedidos pedido);

    // Todas las imágenes de un pedido, ordenadas por número
    List<Imagen> findByPedidoOrderByNumeroDeImagenDelPedidoAsc(Pedidos pedido);

    // Buscar una imagen específica de un pedido por su número
    Optional<Imagen> findByPedidoAndNumeroDeImagenDelPedido(
            Pedidos pedido,
            int numeroDeImagenDelPedido
    );

    // Saber cuántas imágenes tiene un pedido
    int countByPedido(Pedidos pedido);

    // Saber si un pedido tiene imágenes
    boolean existsByPedido(Pedidos pedido);

    // Saber si ya existe determinada posición de imagen en un pedido
    boolean existsByPedidoAndNumeroDeImagenDelPedido(
            Pedidos pedido,
            int numeroDeImagenDelPedido
    );

    // Eliminar todas las imágenes de un pedido
    void deleteByPedido(Pedidos pedido);
}
