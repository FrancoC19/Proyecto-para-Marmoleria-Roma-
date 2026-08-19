package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.PedidoNoEncontrado;
import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Extras.Imagen;
import Marmoleria.Roma.demo.Service.ServiceImagen;
import Marmoleria.Roma.demo.Service.ServicePedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/Imagenes")
public class ControllerImagen {

    @Autowired
    private ServiceImagen serviceImagen;

    @Autowired
    private ServicePedidos servicePedidos;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/TodasDePedido/{idPedido}")
    public List<Imagen> obtenerTodasPedido(@PathVariable long idPedido){
        Pedidos pedido=servicePedidos.pedidoSegunID(idPedido).orElseThrow(()->new PedidoNoEncontrado("El ID del pedido no esta registrado"));
        return serviceImagen.obtenerImagenesDePedido(pedido);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/DePedido/{idPedido}/{numero}")
    public Imagen obtenerImagen(@PathVariable Long idPedido, @PathVariable int numero) {
        Pedidos pedido = servicePedidos.pedidoSegunID(idPedido).orElseThrow(() -> new PedidoNoEncontrado("El ID del pedido no está registrado"));
        return serviceImagen.obtenerImagenDePedido(pedido, numero);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PostMapping( "/Agregar/{idPedido}")
    public void agregarImagen(@PathVariable Long idPedido, @RequestBody String base64) throws IOException {
        Pedidos pedido = servicePedidos.pedidoSegunID(idPedido).orElseThrow(() -> new PedidoNoEncontrado("El ID del pedido no está registrado"));

        serviceImagen.guardarImagen(base64, pedido);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/Actualizar/{idPedido}/{numero}")
    public void actualizarImagen(@PathVariable Long idPedido, @PathVariable int numero, @RequestBody String base64) throws IOException {
        Pedidos pedido = servicePedidos.pedidoSegunID(idPedido).orElseThrow(() -> new PedidoNoEncontrado("El ID del pedido no está registrado"));

        Imagen imagen = serviceImagen.obtenerImagenDePedido(pedido, numero);

        serviceImagen.actualizarImagen(imagen,base64);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @DeleteMapping("/Eliminar/{idPedido}/{numero}")
    public void eliminarImagen(@PathVariable Long idPedido, @PathVariable int numero) {
        Pedidos pedido = servicePedidos.pedidoSegunID(idPedido).orElseThrow(() -> new PedidoNoEncontrado("El ID del pedido no está registrado"));

        Imagen imagen = serviceImagen.obtenerImagenDePedido(pedido, numero);

        serviceImagen.eliminarImagen(imagen);
    }
}
