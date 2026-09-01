package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import Marmoleria.Roma.demo.Repository.RepositoryGrupoPrecio;
import Marmoleria.Roma.demo.Repository.RepositoryMatriales;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class ServiceGrupoPrecio {
    @Autowired
    private RepositoryGrupoPrecio repositoryGrupoPrecio;

    @Autowired
    private RepositoryMatriales repositoryMatriales;

    public List<GrupoPrecio> todosLosGrupos() {
        return repositoryGrupoPrecio.findAll();
    }

    public List<GrupoPrecio> buscarPorTipo(TipoMaterial tipo) {
        return repositoryGrupoPrecio.findByTipoMaterialAndActivoTrue(tipo);
    }

    public GrupoPrecio buscarPorId(long id) {
        return repositoryGrupoPrecio.findById(id).orElse(null);
    }

    public GrupoPrecio guardarGrupo(GrupoPrecio grupo) {
        validarPrecioSegunModo(grupo.isPrecioCompartido(), grupo.getPrecioPorM2());
        return repositoryGrupoPrecio.save(grupo);
    }

    // El precio del grupo solo es obligatorio cuando es compartido; en modo individual
    // cada Materiales tiene su propio precioPorM2Individual y este campo no se usa.
    private void validarPrecioSegunModo(boolean precioCompartido, Float precioPorM2) {
        if (precioCompartido && (precioPorM2 == null || precioPorM2 <= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser cero o negativo");
        }
    }

    @Transactional
    public GrupoPrecio actualizarPrecio(long id, Float nuevoPrecio) {
        GrupoPrecio grupo = repositoryGrupoPrecio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        grupo.setPrecioPorM2(nuevoPrecio);
        return repositoryGrupoPrecio.saveAndFlush(grupo);
    }
    @Transactional
    public GrupoPrecio modificarGrupo(long id, String nombre, Float precioPorM2, Moneda moneda, boolean precioCompartido) {
        validarPrecioSegunModo(precioCompartido, precioPorM2);
        GrupoPrecio grupo = repositoryGrupoPrecio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        grupo.setNombre(nombre);
        grupo.setPrecioPorM2(precioPorM2);
        grupo.setMoneda(moneda);
        grupo.setPrecioCompartido(precioCompartido);
        return repositoryGrupoPrecio.saveAndFlush(grupo);
    }

    @Transactional
    public GrupoPrecio cambiarEstado(long id, boolean activo) {
        GrupoPrecio grupo = repositoryGrupoPrecio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        grupo.setActivo(activo);
        return repositoryGrupoPrecio.saveAndFlush(grupo);
    }

    @Transactional
    public void aplicarAumento(long grupoId, float porcentaje) {
        GrupoPrecio grupo = repositoryGrupoPrecio.findById(grupoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));

        float factor = 1 + (porcentaje / 100f);

        if (grupo.isPrecioCompartido()) {
            // Cuarzo/Sinterizado: un solo precio para actualizar
            grupo.setPrecioPorM2(grupo.getPrecioPorM2() * factor);
            repositoryGrupoPrecio.saveAndFlush(grupo);
        } else {
            // Granito/Mármol: cada material mantiene su propio precio, pero todos escalan igual
            List<Materiales> materiales = repositoryMatriales.findByGrupoPrecio(grupo);
            for (Materiales m : materiales) {
                if (m.getPrecioPorM2Individual() != null) {
                    m.setPrecioPorM2Individual(m.getPrecioPorM2Individual() * factor);
                }
            }
            repositoryMatriales.saveAll(materiales);
        }
    }
}
