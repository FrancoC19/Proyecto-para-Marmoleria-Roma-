package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import Marmoleria.Roma.demo.Service.ServiceGrupoPrecio;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/GruposPrecio")
public class GrupoPrecioController {
    @Autowired
    ServiceGrupoPrecio serviceGrupoPrecio;

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Todos")
    public List<GrupoPrecio> todos() {
        return serviceGrupoPrecio.todosLosGrupos();
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Tipo/{tipo}")
    public List<GrupoPrecio> porTipo(@PathVariable TipoMaterial tipo) {
        return serviceGrupoPrecio.buscarPorTipo(tipo);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<GrupoPrecio> crear(@RequestBody @Valid GrupoPrecio grupo) {
        return ResponseEntity.ok(serviceGrupoPrecio.guardarGrupo(grupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("/ActualizarPrecio/{id}")
    public ResponseEntity<GrupoPrecio> actualizarPrecio(@PathVariable long id, @RequestBody Map<String, Float> body) {
        Float nuevoPrecio = body.get("precioPorM2");
        if (nuevoPrecio == null || nuevoPrecio <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser cero o negativo");
        }
        return ResponseEntity.ok(serviceGrupoPrecio.actualizarPrecio(id, nuevoPrecio));
    }
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Modificar/{id}")
    public ResponseEntity<GrupoPrecio> modificar(@PathVariable long id, @RequestBody Map<String, Object> datos) {
        String nombre = (String) datos.get("nombre");
        Float precio = Float.parseFloat(datos.get("precioPorM2").toString());
        Moneda moneda = Moneda.valueOf((String) datos.get("moneda"));
        boolean precioCompartido = Boolean.parseBoolean(datos.get("precioCompartido").toString());
        return ResponseEntity.ok(serviceGrupoPrecio.modificarGrupo(id, nombre, precio, moneda, precioCompartido));
    }
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Desactivar/{id}")
    public ResponseEntity<GrupoPrecio> desactivar(@PathVariable long id) {
        return ResponseEntity.ok(serviceGrupoPrecio.cambiarEstado(id, false));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Activar/{id}")
    public ResponseEntity<GrupoPrecio> activar(@PathVariable long id) {
        return ResponseEntity.ok(serviceGrupoPrecio.cambiarEstado(id, true));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/AplicarAumento/{id}")
    public ResponseEntity<String> aplicarAumento(@PathVariable long id, @RequestBody Map<String, Float> body) {
        Float porcentaje = body.get("porcentaje");
        if (porcentaje == null) {
            return ResponseEntity.badRequest().body("Debe indicar el porcentaje de aumento");
        }
        serviceGrupoPrecio.aplicarAumento(id, porcentaje);
        return ResponseEntity.ok("Aumento del " + porcentaje + "% aplicado correctamente");
    }
}
