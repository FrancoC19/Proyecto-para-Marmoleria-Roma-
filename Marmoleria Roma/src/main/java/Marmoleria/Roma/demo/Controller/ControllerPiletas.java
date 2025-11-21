package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.PiletaNoEncontrada;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Service.ServicePiletas;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Piletas")
public class ControllerPiletas {

    @Autowired
    ServicePiletas servicePiletas;

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<String> guardarPileta(@RequestBody Piletas piletas){
        Piletas existente = servicePiletas.buscarPorId(piletas.getId());
        if(existente != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe esta Pileta");
        }
        servicePiletas.guardarPileta(piletas);
        return ResponseEntity.ok("Pileta guardado correctamente");
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
    @PutMapping("/Modificar")
    public ResponseEntity<String> modificarPileta(@RequestBody long id_Pileta,@PathVariable Piletas DatosActualizados){
        return Optional.ofNullable(servicePiletas.buscarPorId(id_Pileta))
                .map(pileta->{
                    pileta.setMarca(DatosActualizados.getMarca());
                    pileta.setModelo(DatosActualizados.getModelo());
                    pileta.setCantidad(DatosActualizados.getCantidad());
                    pileta.setAncho(DatosActualizados.getAncho());
                    pileta.setLargo(DatosActualizados.getLargo());
                    return ResponseEntity.ok("Pileta modificada correctamente");
                }).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pileta No Encontrada"));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @PutMapping("/Modicar/{id}")
    public ResponseEntity<String> actualizarStock(@PathVariable int cantidad,@PathVariable long id){
        return Optional.ofNullable(servicePiletas.buscarPorId(id))
                .map(pileta->{
                    pileta.setCantidad(cantidad);
                    return ResponseEntity.ok("Pileta actualizada correctamente");
                }).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pileta No Encontrada"));
    }


}
