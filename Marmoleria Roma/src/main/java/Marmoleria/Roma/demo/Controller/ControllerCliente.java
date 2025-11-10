package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.ClienteNoEncontrado;
import Marmoleria.Roma.demo.Excepciones.DireccionNoEncontrada;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Service.ServiceCliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Cliente")
public class ControllerCliente {

    @Autowired
    private ServiceCliente serviceCliente;


    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarCliente(@RequestBody @Valid Cliente cliente) {
        Cliente existente = serviceCliente.buscarClientePorDNI(cliente.getDNI());

        if (existente != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un cliente con ese DNI");
        }

        serviceCliente.guardarCliente(cliente);
        return ResponseEntity.ok("Cliente guardado correctamente");
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Buscar/{dni}")
    public Cliente buscarClientexDNI(@PathVariable long dni) {
        Cliente existente = serviceCliente.buscarClientePorDNI(dni);

        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un clinete con este DNI");
        }

        return existente;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/Todos")
    public List<Cliente> obtenerTodosLosClientes() {
        return serviceCliente.buscarClientes().orElseThrow(()-> new ClienteNoEncontrado("no se encontraron los Clientes"));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/{dni}")
    public ResponseEntity<String> actualizarCliente(@PathVariable long dni, @RequestBody @Valid Cliente datosActualizados) {
        return Optional.ofNullable(serviceCliente.buscarClientePorDNI(dni))
                .map(cliente -> {
                    // Actualizamos los campos simples
                    cliente.setNombre(datosActualizados.getNombre());
                    cliente.setApellido(datosActualizados.getApellido());
                    cliente.setCorreo(datosActualizados.getCorreo());
                    cliente.setTelefono(datosActualizados.getTelefono());

                    // ✅ Actualizamos la lista de direcciones (si se envía)
                    if (datosActualizados.getDirecciones() != null) {
                        cliente.getDirecciones().clear();
                        cliente.getDirecciones().addAll(datosActualizados.getDirecciones());
                    }

                    // Guardamos los cambios
                    serviceCliente.guardarCliente(cliente);

                    return ResponseEntity.ok("Cliente actualizado correctamente");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cliente no encontrado"));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/clientes/{dni}/direcciones")
    public List<Direccion> obtenerDireccionesCliente(@PathVariable long dni) {
        List<Direccion> direcciones = serviceCliente.buscarDireccionesCliente(dni);
        if (direcciones.isEmpty()) {
            throw new DireccionNoEncontrada("No se encontró ninguna dirección relacionada a este cliente.");
        }
        return direcciones;
    }

}
