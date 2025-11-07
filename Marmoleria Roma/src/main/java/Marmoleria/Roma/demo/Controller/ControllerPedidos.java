package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.FechaIlegal;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.EstadoPedido;
import Marmoleria.Roma.demo.Service.ServicePedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Pedidos")
public class ControllerPedidos {

    @Autowired
    private ServicePedidos servicePedidos;

    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarPedido(@RequestBody Pedidos pedido) {
        servicePedidos.guardarPedidos(pedido);
        return ResponseEntity.ok("Pedido guardado correctamente.");
    }

    @GetMapping("/Todos")
    public ResponseEntity<List<Pedidos>> obtenerTodosLosPedidos() {
        List<Pedidos> pedidos = servicePedidos.todosLosPedidos().orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/Estado/{estado}")
    public ResponseEntity<List<Pedidos>> obtenerPorEstado(@PathVariable EstadoPedido estado) {
        List<Pedidos> pedidos = servicePedidos.pedidosSegunEstado(estado).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/ObtenerPedido/{id}")
    public ResponseEntity<Pedidos> obtenerPorId(@PathVariable long id) {
        return servicePedidos.pedidoSegunID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @GetMapping("/Proximos")
    public ResponseEntity<List<Pedidos>> obtenerPedidosProximos(
            @RequestParam(defaultValue = "7") int dias) {
        List<Pedidos> pedidos = servicePedidos.pedidosProximosAVencer(dias).orElse(List.of());
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/Actualizar/{id}")
    public ResponseEntity<String> actualizarPedido(@PathVariable long id, @RequestBody Pedidos pedidoActualizado) {
        return servicePedidos.pedidoSegunID(id)
                .map(p -> {
                    // 🔹 Campos básicos
                    p.setObservaciones(pedidoActualizado.getObservaciones());
                    p.setSenia(pedidoActualizado.getSenia());
                    p.setGriferia(pedidoActualizado.getGriferia());
                    p.setMoldura(pedidoActualizado.getMoldura());
                    p.setFechaEntrega(pedidoActualizado.getFechaEntrega());
                    p.setFechaEmision(pedidoActualizado.getFechaEmision());
                    p.setMetrosCuadrados(pedidoActualizado.getMetrosCuadrados());
                    p.setDescuento(pedidoActualizado.getDescuento());
                    p.setEstado(pedidoActualizado.getEstado());

                    // 🔹 Relaciones (si vienen actualizadas en el JSON)
                    if (pedidoActualizado.getCliente() != null)
                        p.setCliente(pedidoActualizado.getCliente());

                    if (pedidoActualizado.getEmpleado() != null)
                        p.setEmpleado(pedidoActualizado.getEmpleado());

                    if (pedidoActualizado.getMaterial() != null)
                        p.setMaterial(pedidoActualizado.getMaterial());

                    if (pedidoActualizado.getPileta() != null)
                        p.setPileta(pedidoActualizado.getPileta());

                    if (pedidoActualizado.getDireccion() != null)
                        p.setDireccion(pedidoActualizado.getDireccion());

                    // 🔹 Recalcular valor total del pedido
                    p.calcularValor();

                    // 🔹 Guardar cambios
                    servicePedidos.actualizarPedido(p);

                    return ResponseEntity.ok("Pedido actualizado correctamente.");
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable long id) {
        servicePedidos.eliminarPedido(id);
        return ResponseEntity.ok("Pedido eliminado correctamente.");
    }
}
