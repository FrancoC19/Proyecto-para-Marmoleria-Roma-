package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.PiletaNoEncontrada;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Service.ServicePiletas;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Piletas")
public class ControllerPiletas {

    @Autowired
    ServicePiletas servicePiletas;

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<Map<String, String>> guardarPileta(
            @Valid @RequestBody Piletas piletas,
            Authentication auth,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("Authentication object = " + auth);
        System.out.println("Authorization header = " + authHeader);

        // Validación opcional de duplicados (marca + modelo)
        Optional<List<Piletas>> duplicado =
                servicePiletas.buscarModeloYMarca(piletas.getMarca(), piletas.getModelo());

        if (duplicado.isPresent() && !duplicado.get().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe una pileta con esta marca y modelo");
        }

        servicePiletas.guardarPileta(piletas);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Pileta guardada correctamente");

        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarModelo/{Modelo}")
    public List<Piletas> buscarPileta(@PathVariable String Modelo){
        return servicePiletas.buscarPorModelo(Modelo).orElseThrow(()-> new PiletaNoEncontrada("Este modelo no se encontro: "+Modelo));

    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarMarca/{Marca}")
    public List<Piletas> buscarPiletaPorMarca(@PathVariable String Marca){
        return servicePiletas.buscarPorMarca(Marca).orElseThrow(()->new PiletaNoEncontrada("No se encontro esta marca: "+Marca));

    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarModeloyMarca/{Modelo}/{Marca}")
    public List<Piletas> buscarPorMarcaYModelo(@PathVariable String Modelo, @PathVariable String Marca){
        return servicePiletas.buscarModeloYMarca(Marca,Modelo).orElseThrow(()-> new PiletaNoEncontrada("No se encontro este modelo relacionada con la marca seleccionada"));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Todas")
    public List<Piletas> buscarTodas(){
        return servicePiletas.todasLasPiletas().orElseThrow(()->new PiletaNoEncontrada("No hay Piletas cargadas"));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarId/{id}")
    public Piletas buscarPorId(@PathVariable long id){
        return servicePiletas.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> eliminarPileta(@PathVariable long id){
        Piletas pileta = servicePiletas.buscarPorId(id);
        if(pileta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pileta no encontrada");
        }
        servicePiletas.eliminarPileta(pileta);
        return ResponseEntity.ok("Pileta eliminada correctamente");
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PutMapping("/Modificar/{id_pileta}")
    public ResponseEntity<Piletas> modificarPileta(@PathVariable  long id_pileta,@RequestBody Piletas DatosActualizados){
        return Optional.ofNullable(servicePiletas.buscarPorId(id_pileta))
                .map(pileta->{
                    pileta.setMarca(DatosActualizados.getMarca());
                    pileta.setModelo(DatosActualizados.getModelo());
                    pileta.setCantidad(DatosActualizados.getCantidad());
                    pileta.setAncho(DatosActualizados.getAncho());
                    pileta.setLargo(DatosActualizados.getLargo());
                    servicePiletas.guardarPileta(pileta);
                    return ResponseEntity.ok(pileta);
                }).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/ModificarStock/{id}/{cantidad}")
    public ResponseEntity<String> actualizarStock(@PathVariable long id, @PathVariable int cantidad) {
        return Optional.ofNullable(servicePiletas.buscarPorId(id))
                .map(pileta -> {
                    pileta.setCantidad(cantidad);
                    servicePiletas.guardarPileta(pileta);
                    return ResponseEntity.ok("Stock actualizado correctamente");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pileta no encontrada"));
    }


}
