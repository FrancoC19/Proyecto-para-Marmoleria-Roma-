package Marmoleria.Roma.demo.Controller;
import Marmoleria.Roma.demo.Excepciones.EmpleadoInexistente;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import Marmoleria.Roma.demo.Service.ServiceEmpleado;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/Empleado")
public class ControllerEmpleado {
    @Autowired
    private ServiceEmpleado serviceEmpleado;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarUsuario(@RequestBody @Valid Empleado empleado) {
        Empleado existente = serviceEmpleado.buscarEmpladoPorDNI(empleado.getDNI());

        if (existente != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un empleado con ese DNI");
        }

        serviceEmpleado.guardarEmpleado(empleado);
        return ResponseEntity.ok("Empleado guardado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/Eliminar/{DNI}")
    public ResponseEntity<String> eliminarEmpleadoPorDNI(@PathVariable("DNI") long dni) {
        serviceEmpleado.EliminarEmpleado(dni);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Todos")
    public List<Empleado> obtenerTodos() {
        return serviceEmpleado.ListaDeEmpleados()
                .orElseThrow(() -> new EmpleadoInexistente("No existen empleados registrados en el sistema"));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/ObtenerPorDNI/{DNI}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorDNI(@PathVariable("DNI") long dni) {
        Empleado empleado = serviceEmpleado.buscarEmpladoPorDNI(dni);
        return (empleado != null)
                ? ResponseEntity.ok(empleado)
                : ResponseEntity.notFound().build();
    }

    // 🔹 Nuevo endpoint: buscar por nombre
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarPorNombre/{nombre}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorNombre(@PathVariable String nombre) {
        Empleado empleado = serviceEmpleado.buscraEmpleadoPorNombre(nombre);
        return (empleado != null)
                ? ResponseEntity.ok(empleado)
                : ResponseEntity.notFound().build();
    }

    // 🔹 Nuevo endpoint: buscar por correo
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarPorCorreo/{correo}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorCorreo(@PathVariable String correo) {
        Empleado empleado = serviceEmpleado.buscarPorCorreo(correo);
        return (empleado != null)
                ? ResponseEntity.ok(empleado)
                : ResponseEntity.notFound().build();
    }
}