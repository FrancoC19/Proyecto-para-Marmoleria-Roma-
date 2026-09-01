package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoCalculo;
import Marmoleria.Roma.demo.Modelos.Extras.ItemAdicional;
import Marmoleria.Roma.demo.Service.ServiceItemAdicional;
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
@RequestMapping("/ItemsAdicionales")
public class ControllerItemAdicional {
    @Autowired
    ServiceItemAdicional serviceItemAdicional;

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Todos")
    public List<ItemAdicional> todos() {
        return serviceItemAdicional.todosLosItems();
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Activos")
    public List<ItemAdicional> activos() {
        return serviceItemAdicional.itemsActivos();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<ItemAdicional> crear(@RequestBody @Valid ItemAdicional item) {
        return ResponseEntity.ok(serviceItemAdicional.guardarItem(item));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Modificar/{id}")
    public ResponseEntity<ItemAdicional> modificar(@PathVariable long id, @RequestBody Map<String, Object> datos) {
        String nombre = (String) datos.get("nombre");
        TipoCalculo tipoCalculo = TipoCalculo.valueOf((String) datos.get("tipoCalculo"));
        Float precio = Float.parseFloat(datos.get("precio").toString());
        if (precio <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor a cero");
        }
        Moneda moneda = Moneda.valueOf((String) datos.get("moneda"));
        return ResponseEntity.ok(serviceItemAdicional.modificarItem(id, nombre, tipoCalculo, precio, moneda));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Desactivar/{id}")
    public ResponseEntity<ItemAdicional> desactivar(@PathVariable long id) {
        return ResponseEntity.ok(serviceItemAdicional.cambiarEstado(id, false));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/Activar/{id}")
    public ResponseEntity<ItemAdicional> activar(@PathVariable long id) {
        return ResponseEntity.ok(serviceItemAdicional.cambiarEstado(id, true));
    }
}
