package Marmoleria.Roma.demo.Repository;

import Marmoleria.Roma.demo.Modelos.Extras.PedidoItemAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryPedidoItemAdicional extends JpaRepository<PedidoItemAdicional, Long> {
    List<PedidoItemAdicional> findByPedido_IdPedido(Long idPedido);
}
