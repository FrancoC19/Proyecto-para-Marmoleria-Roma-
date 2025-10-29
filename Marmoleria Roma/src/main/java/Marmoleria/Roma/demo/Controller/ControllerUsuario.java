package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.UsuarioNoEncontrado;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Service.ServiceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Usuario")
public class ControllerUsuario {

    @Autowired
    ServiceUsuario serviceUsuario;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/Todos")
    public List<Usuario> todosLosUsuarios(){
        return serviceUsuario.todosLosUsuarios().orElseThrow(()-> new UsuarioNoEncontrado("No se encontro el usuario"));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarUsuario(@RequestBody Usuario usuario){
        Usuario existente= serviceUsuario.buscarPorId(usuario.getId());

        if(existente!=null){
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe este usuario");
        }
        serviceUsuario.guardarUsuario(usuario);
        return ResponseEntity.ok("Usuario guardado correctamente");

    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/Tipo/{tipo}")
    public List<Usuario> buscarPorTipo(@PathVariable TipoUsuario tipo){
        return serviceUsuario.BuscarPorTipoDeUsuario(tipo).orElseThrow(()-> new UsuarioNoEncontrado("No se encontro el usuario"));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/Email/{email}")
    public Usuario buscarPorEmail(@PathVariable String email){
        return serviceUsuario.BuscarPorEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable long id){
        Usuario usuario = serviceUsuario.buscarPorId(id);
        if(usuario==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el usuario");
        }
        serviceUsuario.eliminarUsuario(usuario);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/Modificar")
    public ResponseEntity<String> modificarUsuario(@RequestBody Usuario DatosActualizados,@PathVariable long id){
       return Optional.ofNullable(serviceUsuario.buscarPorId(id))
               .map(usuario->{
                   DatosActualizados.setEmail(usuario.getEmail());
                   DatosActualizados.setContra(usuario.getContra());
                   return ResponseEntity.ok("Usuario modificado correctamente");
               }).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el usuario"));

    }






}
