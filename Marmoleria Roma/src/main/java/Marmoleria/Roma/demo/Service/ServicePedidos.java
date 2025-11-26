package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.FechaIlegal;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Extras.Notificacion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import Marmoleria.Roma.demo.Repository.RepositoryPedidos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicePedidos {

    @Autowired
    private RepositoryPedidos repositoryPedidos;

    @Autowired
    private EmailService emailService;

    @Autowired
    @Lazy
    private NotificacionService notificacionService;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void guardarPedidos(Pedidos pedido) {
        // Validar y buscar Cliente
        if (pedido.getCliente() == null || pedido.getCliente().getDNI() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio");
        }
        Cliente clientePersistido = entityManager.find(Cliente.class, pedido.getCliente().getDNI());
        if (clientePersistido == null) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + pedido.getCliente().getDNI());
        }
        pedido.setCliente(clientePersistido);

        // Validar y buscar Empleado
        if (pedido.getEmpleado() == null || pedido.getEmpleado().getDNI() == null) {
            throw new IllegalArgumentException("El ID del empleado es obligatorio");
        }
        Empleado empleadoPersistido = entityManager.find(Empleado.class, pedido.getEmpleado().getDNI());
        if (empleadoPersistido == null) {
            throw new IllegalArgumentException("Empleado no encontrado con ID: " + pedido.getEmpleado().getDNI());
        }
        pedido.setEmpleado(empleadoPersistido);

        // Validar y buscar Material
        if (pedido.getMaterial() == null || pedido.getMaterial().getId() == null) {
            throw new IllegalArgumentException("El ID del material es obligatorio");
        }
        Materiales materialPersistido = entityManager.find(Materiales.class, pedido.getMaterial().getId());
        if (materialPersistido == null) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + pedido.getMaterial().getId());
        }
        pedido.setMaterial(materialPersistido);

        // Validar y buscar Pileta
        if (pedido.getPileta() == null || pedido.getPileta().getId() == null) {
            throw new IllegalArgumentException("El ID de la pileta es obligatorio");
        }
        Piletas piletaPersistida = entityManager.find(Piletas.class, pedido.getPileta().getId());
        if (piletaPersistida == null) {
            throw new IllegalArgumentException("Pileta no encontrada con ID: " + pedido.getPileta().getId());
        }
        pedido.setPileta(piletaPersistida);

        // Guardar Pedido
        repositoryPedidos.save(pedido);

        notificacionService.notificacarPorLlamada();
    }



    public Optional<List<Pedidos>> todosLosPedidos() {
        return Optional.of(repositoryPedidos.findAll());
    }

    public Optional<List<Pedidos>> PedidosPendienteYEnProceso() {
        return Optional.of(repositoryPedidos.findByEstadoNot(EstadoPedido.ENTREGADO.toString()));
    }

    public Optional<List<Pedidos>> PedidosTerminados() {
        return Optional.of(repositoryPedidos.findByEstado(EstadoPedido.ENTREGADO.toString()));
    }

    public Optional<List<Pedidos>> pedidosSegunEstado(EstadoPedido estado) {
        return Optional.of(repositoryPedidos.findByEstado(estado.toString()));
    }

    public void eliminarPedido(long idPedido) {
        repositoryPedidos.deleteById(idPedido);
    }

    public Optional<List<Pedidos>> pedidosSegunMaterial(Materiales materiales) {
        return Optional.of(repositoryPedidos.findByMaterial(materiales));
    }

    public Optional<List<Pedidos>> pedidosSegunEmpleado(Empleado empleado) {
        return Optional.of(repositoryPedidos.findByEmpleado(empleado));
    }

    public Optional<List<Pedidos>> pedidosSegunClienteyEmpleado(Cliente cliente, Empleado empleado) {
        return Optional.of(repositoryPedidos.findByClienteAndEmpleado(cliente, empleado));
    }

    public Optional<List<Pedidos>> pedidosSegunCliente(Cliente cliente) {
        return Optional.of(repositoryPedidos.findByCliente(cliente));
    }

    public Optional<Pedidos> pedidoSegunID(long idPedido) {
        return repositoryPedidos.findById(idPedido);
    }

    public Optional<List<Pedidos>> pedidosSegunFecha(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new FechaIlegal("La fecha no puede ser nula");
        }
        if (inicio.isAfter(fin)) {
            throw new FechaIlegal("La fecha de inicio debe ser anterior a la fecha final");
        }
        return Optional.of(repositoryPedidos.findByFechaEntregaBetween(inicio, fin));
    }

    public void actualizarPedido(Pedidos pedido) {
        String estadoActual = pedido.getEstado();

        if (estadoActual.equalsIgnoreCase(EstadoPedido.EN_PROCESO.toString())) {
            pedido.setEstado(EstadoPedido.PENDIENTE_DE_ENTREGA.toString());
            emailService.enviarCorreoSimple(pedido.getCliente().getCorreo(),"Pedido terminado","Por la presente, le notificamos a: "+pedido.getCliente().getNombre()+", de correo: "+pedido.getCliente().getCorreo()+" y telefono: "+pedido.getCliente().getTelefono()+" que su pedido esta preparado para la entrega, coordinar por whatsapp con la secretaria \n\nAtentamente,\nMarmoleria Roma");
        } else if (estadoActual.equalsIgnoreCase(EstadoPedido.PENDIENTE_DE_ENTREGA.toString())) {
            pedido.setEstado(EstadoPedido.ENTREGADO.toString());
        }

        repositoryPedidos.save(pedido);
        notificacionService.notificacarPorLlamada();
    }

    public Optional<List<Pedidos>> pedidosProximosAVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);

        List<Pedidos> pedidos = repositoryPedidos.findAll()
                .stream()
                .filter(p ->
                        //Parseo la fecha de string a local date
                        // Fecha dentro del rango
                        !p.getFechaEntrega().isBefore(hoy) &&
                                !p.getFechaEntrega().isAfter(limite) &&

                                // Estado solo "EN_PROCESO"
                        p.getEstado().equalsIgnoreCase(EstadoPedido.EN_PROCESO.toString())
                )
                .toList();

        return Optional.of(pedidos);
    }


}
