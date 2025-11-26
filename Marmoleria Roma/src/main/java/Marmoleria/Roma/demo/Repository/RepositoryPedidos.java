package Marmoleria.Roma.demo.Repository;

import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepositoryPedidos extends JpaRepository<Pedidos, Long> {

    List<Pedidos> findByCliente(Cliente cliente);

    List<Pedidos> findByEmpleado(Empleado empleado);

    List<Pedidos> findByClienteAndEmpleado(Cliente cliente, Empleado empleado);

    List<Pedidos> findByMaterial(Materiales materiales);

    List<Pedidos> findByEstado(String estado);

    List<Pedidos> findByFechaEntregaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Pedidos> findByEstadoNot(String estado);
}
