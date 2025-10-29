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
        return ResponseEntity.ok("Cliente guardado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/Eliminar/{DNI}")
    public ResponseEntity<String> eliminarEmpleadoPorDNI(@RequestParam("DNI") long dni) {
        serviceEmpleado.EliminarEmpleado(dni);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }

    @PreAuthorize("hasAnyRole('USUARIO,ADMINISTRADOR')")
    @GetMapping("/todos")
    public List<Empleado> obtenerTodos(){
        return serviceEmpleado.ListaDeEmpleados().orElseThrow(()-> new EmpleadoInexistente("No existen empleados registrados en el sistema"));
    }

    @PreAuthorize("hasAnyRole('USUARIO,ADMINISTRADOR')")
    @GetMapping("/ObtenerPorDNI/{DNI}")
    public Empleado obtenerEmpleadoPorDNI(long dni) {
        return serviceEmpleado.buscarEmpladoPorDNI(dni);
    }
}
