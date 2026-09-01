package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.FechaIlegal;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Modelos.Extras.ItemAdicional;
import Marmoleria.Roma.demo.Modelos.Extras.PedidoItemAdicional;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import Marmoleria.Roma.demo.Modelos.dtos.CotizacionDTO;
import Marmoleria.Roma.demo.Modelos.dtos.CotizacionResultadoDTO;
import Marmoleria.Roma.demo.Modelos.dtos.PedidoDTO;
import Marmoleria.Roma.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/Pedidos")
public class ControllerPedidos {

    @Autowired
    private ServicePedidos servicePedidos;

    @Autowired
    private ServiceCliente serviceCliente;

    @Autowired
    private ServiceEmpleado serviceEmpleado;

    @Autowired
    private ServiceMateriales serviceMateriales;
    @Autowired
    private ServicePiletas servicePiletas;
    @Autowired
    private ServiceItemAdicional serviceItemAdicional;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarPedido(@RequestBody PedidoDTO dto) {

        Cliente cliente = serviceCliente.buscarClientePorDNI(dto.clienteDni);
        if (cliente == null) return ResponseEntity.badRequest().body("Cliente no encontrado");

        // Empleado NO se asigna al cargar el pedido: se asigna recién al finalizar el proceso.
        Empleado empleado = null;

        // Pileta opcional (mesada ciega o pileta de cliente si no viene ID)
        Piletas pileta = null;
        if (dto.piletaId != null) {
            pileta = servicePiletas.buscarPorId(dto.piletaId);
            if (pileta == null) return ResponseEntity.badRequest().body("Pileta no encontrada");

            int nuevoStock = pileta.getCantidad() - 1;
            if (nuevoStock < 0) return ResponseEntity.badRequest().body("Stock insuficiente para la pileta seleccionada");

            pileta.setCantidad(nuevoStock);
            servicePiletas.modificarPileta(pileta);
        }

        Materiales material = serviceMateriales.buscarPorId(dto.materialId);
        if (material == null) return ResponseEntity.badRequest().body("Material no encontrado");

        Pedidos pedido = new Pedidos(cliente, empleado, dto.descuento, dto.fechaEmision, dto.fechaEntrega,
                dto.griferia, material, dto.metrosCuadrados, dto.moldura, dto.observaciones,
                pileta, dto.senia, dto.direccion);
        pedido.setPiletaDeCliente(pileta == null && Boolean.TRUE.equals(dto.piletaDeCliente));

        // Armar los ítems adicionales (mano de obra) elegidos para este pedido
        if (dto.itemsAdicionales != null) {
            List<PedidoItemAdicional> items = new ArrayList<>();
            for (PedidoDTO.ItemAdicionalDTO itemDto : dto.itemsAdicionales) {
                PedidoItemAdicional pia;
                if (itemDto.itemAdicionalId != null) {
                    ItemAdicional itemCatalogo = serviceItemAdicional.buscarPorId(itemDto.itemAdicionalId);
                    if (itemCatalogo == null) {
                        return ResponseEntity.badRequest().body("Ítem adicional no encontrado: " + itemDto.itemAdicionalId);
                    }
                    pia = new PedidoItemAdicional(pedido, itemCatalogo, itemDto.cantidad);
                } else {
                    if (itemDto.precioManual == null || itemDto.precioManual < 0) {
                        return ResponseEntity.badRequest().body("El precio manual no puede ser negativo");
                    }
                    pia = new PedidoItemAdicional(pedido, itemDto.descripcionManual, itemDto.precioManual, itemDto.monedaManual);
                }
                items.add(pia);
            }
            pedido.setItemsAdicionales(items);
        }

        servicePedidos.guardarPedidos(pedido);
        return ResponseEntity.ok("Pedido guardado correctamente y stock actualizado.");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PostMapping("/Cotizar")
    public ResponseEntity<CotizacionResultadoDTO> cotizarPedido(@RequestBody CotizacionDTO dto) {
        if (dto.materialId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del material es obligatorio");
        }
        Materiales material = serviceMateriales.buscarPorId(dto.materialId);
        if (material == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Material no encontrado");
        }

        Piletas pileta = null;
        if (dto.piletaId != null) {
            pileta = servicePiletas.buscarPorId(dto.piletaId);
            if (pileta == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pileta no encontrada");
            }
        }

        List<PedidoItemAdicional> items = new ArrayList<>();
        if (dto.itemsAdicionales != null) {
            for (PedidoDTO.ItemAdicionalDTO itemDto : dto.itemsAdicionales) {
                PedidoItemAdicional pia;
                if (itemDto.itemAdicionalId != null) {
                    ItemAdicional itemCatalogo = serviceItemAdicional.buscarPorId(itemDto.itemAdicionalId);
                    if (itemCatalogo == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Ítem adicional no encontrado: " + itemDto.itemAdicionalId);
                    }
                    pia = new PedidoItemAdicional(null, itemCatalogo, itemDto.cantidad);
                } else {
                    if (itemDto.precioManual == null || itemDto.precioManual < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El precio manual no puede ser negativo");
                    }
                    pia = new PedidoItemAdicional(null, itemDto.descripcionManual, itemDto.precioManual, itemDto.monedaManual);
                }
                items.add(pia);
            }
        }

        CotizacionResultadoDTO resultado = servicePedidos.calcularCotizacion(material, pileta, dto.metrosCuadrados, items);
        return ResponseEntity.ok(resultado);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/PendientesAterminar")
    public ResponseEntity<List<Pedidos>> obtenerPedidosaTerminar() {
        List<Pedidos> pedidos = servicePedidos.pedidosSegunEstado(EstadoPedido.EN_PROCESO).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/EnProceso")
    public ResponseEntity<List<Pedidos>> obtenerPedidosEnProceso() {
        List<Pedidos> pedidos = servicePedidos.pedidosSegunEstado(EstadoPedido.PENDIENTE_DE_ENTREGA).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/PedidosTerminados")
    public ResponseEntity<List<Pedidos>> obtenerPedidosTerminado() {
        List<Pedidos> pedidos = servicePedidos.PedidosTerminados().orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Todos")
    public ResponseEntity<List<Pedidos>> obtenerTodosLosPedidos() {
        List<Pedidos> pedidos = servicePedidos.todosLosPedidos().orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Estado/{estado}")
    public ResponseEntity<List<Pedidos>> obtenerPorEstado(@PathVariable EstadoPedido estado) {
        List<Pedidos> pedidos = servicePedidos.pedidosSegunEstado(estado).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/ObtenerPedido/{id}")
    public ResponseEntity<Pedidos> obtenerPorId(@PathVariable long id) {
        return servicePedidos.pedidoSegunID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Fecha")
    public ResponseEntity<List<Pedidos>> obtenerPorRangoDeFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            List<Pedidos> pedidos = servicePedidos.pedidosSegunFecha(inicio, fin).orElse(List.of());
            return ResponseEntity.ok(pedidos);
        } catch (FechaIlegal e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Proximos")
    public ResponseEntity<List<Pedidos>> obtenerPedidosProximos(
            @RequestParam(defaultValue = "7") int dias) {
        List<Pedidos> pedidos = servicePedidos.pedidosProximosAVencer(dias).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/ActualizarDatos/{id}")
    public ResponseEntity<String> actualizarPedido(@PathVariable long id, @RequestBody PedidoDTO dto) {
        return servicePedidos.pedidoSegunID(id)
                .map(p -> {
                    // Campos básicos
                    p.setObservaciones(dto.observaciones);
                    p.setSenia(dto.senia);
                    p.setGriferia(dto.griferia);
                    p.setMoldura(dto.moldura);
                    p.setFechaEntrega(dto.fechaEntrega);
                    p.setMetrosCuadrados(dto.metrosCuadrados);
                    p.setDescuento(dto.descuento);

                    Cliente cliente = serviceCliente.buscarClientePorDNI(dto.clienteDni);
                    if (cliente == null) return ResponseEntity.badRequest().body("Cliente no encontrado");

                    Materiales material = serviceMateriales.buscarPorId(dto.materialId);
                    if (material == null) return ResponseEntity.badRequest().body("Material no encontrado");

                    // --- Manejo de stock de pileta, contemplando que puede ser opcional ---
                    Piletas piletaVieja = p.getPileta(); // puede ser null (mesada ciega previa)
                    Piletas piletaNueva = null;

                    if (dto.piletaId != null) {
                        piletaNueva = servicePiletas.buscarPorId(dto.piletaId);
                        if (piletaNueva == null) return ResponseEntity.badRequest().body("Pileta no encontrada");
                    }

                    boolean cambioDePileta = (piletaVieja == null && piletaNueva != null)
                            || (piletaVieja != null && piletaNueva == null)
                            || (piletaVieja != null && piletaNueva != null && !piletaVieja.getId().equals(piletaNueva.getId()));

                    if (cambioDePileta) {
                        // Devolvemos stock de la pileta anterior, si había una
                        if (piletaVieja != null) {
                            Piletas piletaAReingresar = servicePiletas.buscarPorId(piletaVieja.getId());
                            piletaAReingresar.setCantidad(piletaAReingresar.getCantidad() + 1);
                            servicePiletas.modificarPileta(piletaAReingresar);
                        }
                        // Restamos stock de la pileta nueva, si corresponde
                        if (piletaNueva != null) {
                            int nuevoStock = piletaNueva.getCantidad() - 1;
                            if (nuevoStock < 0) return ResponseEntity.badRequest().body("Stock insuficiente para la pileta seleccionada");
                            piletaNueva.setCantidad(nuevoStock);
                            servicePiletas.modificarPileta(piletaNueva);
                        }
                    }

                    p.setCliente(cliente);
                    p.setMaterial(material);
                    p.setPileta(piletaNueva); // null si es mesada ciega o pileta de cliente
                    p.setPiletaDeCliente(piletaNueva == null && Boolean.TRUE.equals(dto.piletaDeCliente));

                    servicePedidos.actualizarPedidos(p);

                    return ResponseEntity.ok("Pedido actualizado correctamente.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable long id) {
        servicePedidos.eliminarPedido(id);
        return ResponseEntity.ok("Pedido eliminado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Material/{idMaterial}")
    public ResponseEntity<List<Pedidos>> obtenerPorMaterial(@PathVariable long idMaterial) {
        var material = serviceMateriales.buscarPorId(idMaterial);
        if (material == null)
            return ResponseEntity.notFound().build();

        List<Pedidos> pedidos = servicePedidos.pedidosSegunMaterial(material).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Empleado/{dni}")
    public ResponseEntity<List<Pedidos>> obtenerPorEmpleado(@PathVariable long dni) {
        var empleado = serviceEmpleado.buscarEmpleadoPorDNI(dni);
        if (empleado == null)
            return ResponseEntity.notFound().build();

        List<Pedidos> pedidos = servicePedidos.pedidosSegunEmpleado(empleado).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Cliente/{dni}")
    public ResponseEntity<List<Pedidos>> obtenerPorCliente(@PathVariable long dni) {
        var cliente = serviceCliente.buscarClientePorDNI(dni);

        if (cliente == null)
            return ResponseEntity.notFound().build();

        List<Pedidos> pedidos = servicePedidos.pedidosSegunCliente(cliente).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/ClienteEmpleado")
    public ResponseEntity<List<Pedidos>> obtenerPorClienteYEmpleado(@RequestParam long dniCliente, @RequestParam long dniEmpleado) {

        var cliente = serviceCliente.buscarClientePorDNI(dniCliente);
        var empleado = serviceEmpleado.buscarEmpleadoPorDNI(dniEmpleado);

        if (cliente == null || empleado == null)
            return ResponseEntity.notFound().build();

        List<Pedidos> pedidos = servicePedidos.pedidosSegunClienteyEmpleado(cliente, empleado).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PutMapping("/FinalizarProceso/{id}")
    public ResponseEntity<Map<String, String>> finalizarProcesoPedido(@PathVariable Long id, @RequestBody Map<String, Long> body) {

        Long empleadoDni = body.get("empleadoDni");
        if (empleadoDni == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el empleado que finaliza el proceso");
        }
        Empleado empleado = serviceEmpleado.buscarEmpleadoPorDNI(empleadoDni);
        if (empleado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empleado no encontrado");
        }

        Pedidos pedido = servicePedidos.pedidoSegunID(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe ese pedido"
                ));

        pedido.setEmpleado(empleado);
        servicePedidos.actualizarEstadoPedido(pedido);

        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", "Pedido finalizado correctamente");

        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("/EntregarPedido/{id}")
    public ResponseEntity<Map<String, String>> finalizarPedido(@PathVariable Long id) {

        Pedidos pedido = servicePedidos.pedidoSegunID(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe ese pedido"
                ));

        servicePedidos.pedidoEntregado(pedido);

        Map<String, String> resp = new HashMap<>();
        resp.put("mensaje", "Pedido finalizado correctamente");

        return ResponseEntity.ok(resp);
    }
}
