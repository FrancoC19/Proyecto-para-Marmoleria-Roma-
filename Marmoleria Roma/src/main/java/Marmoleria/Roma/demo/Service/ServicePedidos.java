package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.FechaIlegal;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Extras.PedidoItemAdicional;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoCalculo;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import Marmoleria.Roma.demo.Modelos.Extras.ItemAdicional;
import Marmoleria.Roma.demo.Modelos.Extras.Notificacion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import Marmoleria.Roma.demo.Modelos.dtos.CotizacionResultadoDTO;
import Marmoleria.Roma.demo.Repository.RepositoryPedidos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

    @Autowired
    private TipoCambioService tipoCambioService;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Calcula el valor total del pedido convirtiendo todo lo que esté en dólares
     * a pesos, usando la cotización actual. Se recalcula cada vez que se llama,
     * nunca queda "congelado".
     */
    public float calcularValorTotal(Pedidos pedido) {
        Float cotizacionDolar = tipoCambioService.obtenerCotizacionDolar();

        // --- Material ---
        Materiales material = pedido.getMaterial();
        GrupoPrecio grupo = material.getGrupoPrecio();
        float precioMaterialUnitario = material.getValorMetroCuadrado();
        if (grupo.getMoneda() == Moneda.USD) {
            precioMaterialUnitario *= cotizacionDolar;
        }
        float totalMaterial = pedido.getMetrosCuadrados() * precioMaterialUnitario;

        // --- Pileta (opcional, siempre en pesos) ---
        float totalPileta = (pedido.getPileta() != null && pedido.getPileta().getValor() != null)
                ? pedido.getPileta().getValor()
                : 0f;

        // --- Ítems adicionales (mano de obra) ---
        float totalItems = 0f;
        if (pedido.getItemsAdicionales() != null) {
            for (PedidoItemAdicional pia : pedido.getItemsAdicionales()) {
                totalItems += calcularValorItem(pia, cotizacionDolar);
            }
        }

        float resultado = totalMaterial + totalPileta + totalItems;

        if (pedido.getDescuento() != null && pedido.getDescuento() > 0) {
            resultado -= resultado * pedido.getDescuento();
        }

        return resultado;
    }

    private float calcularValorItem(PedidoItemAdicional pia, Float cotizacionDolar) {
        MontoMoneda bruto = valorBrutoItem(pia);
        return convertirMoneda(bruto.valor(), bruto.moneda(), Moneda.ARS, cotizacionDolar);
    }

    // Monto de un ítem adicional en su moneda original, sin convertir todavía.
    private record MontoMoneda(float valor, Moneda moneda) {}

    private MontoMoneda valorBrutoItem(PedidoItemAdicional pia) {
        float precioBase;
        Moneda moneda;
        TipoCalculo tipoCalculo;
        float cantidad = (pia.getCantidad() != null) ? pia.getCantidad() : 1f;

        if (pia.getItemAdicional() != null) {
            // Ítem catalogado
            ItemAdicional item = pia.getItemAdicional();
            precioBase = item.getPrecio();
            moneda = item.getMoneda();
            tipoCalculo = item.getTipoCalculo();
        } else {
            // Caso puntual / manual
            precioBase = (pia.getPrecioManual() != null) ? pia.getPrecioManual() : 0f;
            moneda = pia.getMonedaManual();
            tipoCalculo = TipoCalculo.FIJO; // los manuales se cargan como monto final directo
        }

        float valor = switch (tipoCalculo) {
            case FIJO -> precioBase;
            case POR_METRO_LINEAL, POR_METRO_CUADRADO, POR_CANTIDAD -> precioBase * cantidad;
        };
        return new MontoMoneda(valor, moneda);
    }

    // Convierte un monto entre ARS y USD según la cotización vigente. Si origen == destino, no hace nada.
    private float convertirMoneda(float valor, Moneda origen, Moneda destino, float cotizacionDolar) {
        if (origen == destino) return valor;
        if (origen == Moneda.USD && destino == Moneda.ARS) return valor * cotizacionDolar;
        return valor / cotizacionDolar; // ARS -> USD
    }

    /**
     * Cotiza un pedido (material + pileta opcional + mano de obra) sin persistir nada.
     * El resultado queda expresado en la moneda del material: si el material cotiza en
     * dólares, todo lo que esté en pesos (pileta, ítems en ARS) se convierte a dólares;
     * si el material cotiza en pesos, todo se lleva a pesos.
     */
    public CotizacionResultadoDTO calcularCotizacion(Materiales material, Piletas pileta, Float metrosCuadrados,
                                                      List<PedidoItemAdicional> itemsAdicionales) {
        GrupoPrecio grupo = material.getGrupoPrecio();
        Moneda monedaFinal = grupo.getMoneda();
        Float cotizacionDolar = tipoCambioService.obtenerCotizacionDolar();

        Float precioMaterialUnitario = material.getValorMetroCuadrado();
        if (precioMaterialUnitario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El material no tiene un precio configurado");
        }
        float metros = (metrosCuadrados != null) ? metrosCuadrados : 0f;
        // El precio del material ya está expresado en la moneda de su grupo (monedaFinal)
        float totalMaterial = metros * precioMaterialUnitario;

        float totalPileta = 0f;
        if (pileta != null && pileta.getValor() != null) {
            // El valor de la pileta siempre se carga en pesos
            totalPileta = convertirMoneda(pileta.getValor(), Moneda.ARS, monedaFinal, cotizacionDolar);
        }

        float totalItems = 0f;
        if (itemsAdicionales != null) {
            for (PedidoItemAdicional pia : itemsAdicionales) {
                MontoMoneda bruto = valorBrutoItem(pia);
                totalItems += convertirMoneda(bruto.valor(), bruto.moneda(), monedaFinal, cotizacionDolar);
            }
        }

        return new CotizacionResultadoDTO(monedaFinal, totalMaterial, totalPileta, totalItems);
    }


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

        // Pileta: AHORA ES OPCIONAL (mesada ciega). Solo se resuelve si vino un ID.
        if (pedido.getPileta() != null && pedido.getPileta().getId() != null) {
            Piletas piletaPersistida = entityManager.find(Piletas.class, pedido.getPileta().getId());
            if (piletaPersistida == null) {
                throw new IllegalArgumentException("Pileta no encontrada con ID: " + pedido.getPileta().getId());
            }
            pedido.setPileta(piletaPersistida);
        } else {
            pedido.setPileta(null);
        }

        // Resolver cada ítem adicional contra el catálogo persistido
        if (pedido.getItemsAdicionales() != null) {
            for (PedidoItemAdicional pia : pedido.getItemsAdicionales()) {
                pia.setPedido(pedido);
                if (pia.getItemAdicional() != null && pia.getItemAdicional().getId() != 0) {
                    ItemAdicional itemPersistido = entityManager.find(ItemAdicional.class, pia.getItemAdicional().getId());
                    if (itemPersistido == null) {
                        throw new IllegalArgumentException("Ítem adicional no encontrado con ID: " + pia.getItemAdicional().getId());
                    }
                    pia.setItemAdicional(itemPersistido);
                }
            }
        }

        // Calcular el total con la cotización actual antes de guardar
        pedido.setValorTotalCalculado(calcularValorTotal(pedido));

        repositoryPedidos.save(pedido);
        notificacionService.notificacarPorLlamada();
    }

    @Transactional
    public void actualizarPedidos(Pedidos pedido) {
        try {
            // Recalculamos el total por si cambió algo (material, ítems, descuento, o el dólar)
            pedido.setValorTotalCalculado(calcularValorTotal(pedido));
            repositoryPedidos.saveAndFlush(pedido);
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El pedido fue modificado por otro usuario"
            );
        }

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
        List<Pedidos> retornar = repositoryPedidos.findByEstado(estado.toString()).stream()
                .filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
    }

    public void eliminarPedido(long idPedido) {
        repositoryPedidos.deleteById(idPedido);
    }

    public Optional<List<Pedidos>> pedidosSegunMaterial(Materiales materiales) {
        List<Pedidos> retornar = repositoryPedidos.findByMaterial(materiales)
                .stream().filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
    }

    public Optional<List<Pedidos>> pedidosSegunEmpleado(Empleado empleado) {
        List<Pedidos> retornar = repositoryPedidos.findByEmpleado(empleado)
                .stream().filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
    }

    public Optional<List<Pedidos>> pedidosSegunClienteyEmpleado(Cliente cliente, Empleado empleado) {
        List<Pedidos> retornar = repositoryPedidos.findByClienteAndEmpleado(cliente, empleado)
                .stream().filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
    }

    public Optional<List<Pedidos>> pedidosSegunCliente(Cliente cliente) {
        List<Pedidos> retornar = repositoryPedidos.findByCliente(cliente)
                .stream().filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
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
        List<Pedidos> retornar = repositoryPedidos.findByFechaEntregaBetween(inicio, fin).stream()
                .filter(p -> !p.getEstado().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())).toList();
        return Optional.of(retornar);
    }

    @Transactional
    public void actualizarEstadoPedido(Pedidos pedido) {
        String estadoActual = pedido.getEstado();
        try {
            if (estadoActual.equalsIgnoreCase(EstadoPedido.EN_PROCESO.toString())) {
                pedido.setEstado(EstadoPedido.PENDIENTE_DE_ENTREGA.toString());
            } else if (estadoActual.equalsIgnoreCase(EstadoPedido.PENDIENTE_DE_ENTREGA.toString())) {
                pedido.setEstado(EstadoPedido.ENTREGADO.toString());
            }

            repositoryPedidos.saveAndFlush(pedido);
            notificacionService.notificacarPorLlamada();
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El pedido fue modificado por otro usuario"
            );
        }
    }

    public Optional<List<Pedidos>> pedidosProximosAVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);

        List<Pedidos> pedidos = repositoryPedidos.findAll()
                .stream()
                .filter(p ->
                        !p.getFechaEntrega().isBefore(hoy) &&
                                !p.getFechaEntrega().isAfter(limite) &&
                                p.getEstado().equalsIgnoreCase(EstadoPedido.EN_PROCESO.toString())
                )
                .toList();

        return Optional.of(pedidos);
    }
}