package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Excepciones.MaterialNoEncontrado;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import Marmoleria.Roma.demo.Service.ServiceGrupoPrecio;
import Marmoleria.Roma.demo.Service.ServiceMateriales;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Materiales")
public class ControllerMateriales {

    @Autowired
    ServiceMateriales serviceMateriales;
    @Autowired
    ServiceGrupoPrecio serviceGrupoPrecio;


    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PostMapping("/Guardar")
    public ResponseEntity<Map<String, String>> guardarMateriales(@RequestBody Map<String, Object> datos) {
        String nombreMaterial = (String) datos.get("nombreMaterial");
        TipoMaterial tipo = TipoMaterial.valueOf((String) datos.get("tipoMaterial"));
        long grupoId = Long.parseLong(datos.get("grupoPrecioId").toString());

        GrupoPrecio grupo = serviceGrupoPrecio.buscarPorId(grupoId);
        if (grupo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grupo de precio inválido");
        }

        Materiales material = new Materiales(nombreMaterial, tipo, grupo);

        // Si el grupo NO es de precio compartido, el material necesita su propio precio
        if (!grupo.isPrecioCompartido()) {
            Object precioObj = datos.get("precioPorM2Individual");
            if (precioObj == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Este tipo de material requiere un precio individual");
            }
            float precioIndividual = Float.parseFloat(precioObj.toString());
            if (precioIndividual <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El precio individual debe ser mayor a cero");
            }
            material.setPrecioPorM2Individual(precioIndividual);
        }

        serviceMateriales.guardarMaterial(material);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Material guardado correctamente");
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Buscar/{id}")
    public Materiales buscarMaterial(@PathVariable int id) {

        Materiales Existente= serviceMateriales.buscarPorId(id);
        if(Existente==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se encuentra el material");
        }
        return Existente;
    }



    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/BuscarNombre/{Nombre}")
    public Materiales buscarMateriales(@PathVariable String Nombre) {
        Materiales Existente= serviceMateriales.buscarPorNombre(Nombre);
        if(Existente==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se encuentra el material");
        }
        return Existente;
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Todos")
    public List<Materiales> obtenerTodosLosMateriales(){
        return serviceMateriales.todosLosMateriales().orElseThrow(()->new MaterialNoEncontrado("No hay materiales"));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PutMapping("/Modificar/{id_Material}")
    public ResponseEntity<Materiales> modificarMaterial(@RequestBody Map<String, Object> datos, @PathVariable int id_Material) {
        return Optional.ofNullable(serviceMateriales.buscarPorId(id_Material))
                .map(material -> {
                    material.setNombreMaterial((String) datos.get("nombreMaterial"));
                    material.setTipoMaterial(TipoMaterial.valueOf((String) datos.get("tipoMaterial")));

                    long grupoId = Long.parseLong(datos.get("grupoPrecioId").toString());
                    GrupoPrecio grupo = serviceGrupoPrecio.buscarPorId(grupoId);
                    if (grupo == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grupo de precio inválido");
                    }
                    material.setGrupoPrecio(grupo);

                    // Si el grupo NO es de precio compartido, actualizamos el precio individual
                    if (!grupo.isPrecioCompartido()) {
                        Object precioObj = datos.get("precioPorM2Individual");
                        if (precioObj == null) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Este tipo de material requiere un precio individual");
                        }
                        float precioIndividual = Float.parseFloat(precioObj.toString());
                        if (precioIndividual <= 0) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "El precio individual debe ser mayor a cero");
                        }
                        material.setPrecioPorM2Individual(precioIndividual);
                    } else {
                        // Si pasó a un grupo compartido, limpiamos el precio individual viejo (ya no aplica)
                        material.setPrecioPorM2Individual(null);
                    }

                    serviceMateriales.actualizarMaterial(material);
                    return ResponseEntity.ok(material);
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Tipo/{TipoMaterial}")
    public List<Materiales> obtenerTiposMateriales(@PathVariable TipoMaterial TipoMaterial) {
        return serviceMateriales.buscarPorTipoDeMaterial(TipoMaterial).orElseThrow(()->new MaterialNoEncontrado("No hay de este tipo"));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @GetMapping("/Precio/{precio}")
    public List<Materiales> obtenerPreciosMateriales(@PathVariable float precio) {
        return serviceMateriales.buscarPorValorMetroCuadrado(precio);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/Eliminar/{nombre}")
    public ResponseEntity<String> eliminarMaterial(@PathVariable String nombre) {
        Materiales material= serviceMateriales.buscarPorNombre(nombre);
        if(material!=null) {
            serviceMateriales.eliminarMaterial(material);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encuentra el material");
        }
        return ResponseEntity.ok("Material eliminado correctamente");
    }




}
