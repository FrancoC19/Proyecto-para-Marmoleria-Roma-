package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Repository.RepositoryMatriales;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceMateriales {
    @Autowired
    private RepositoryMatriales repositoryMatriales;

    public Optional<List<Materiales>> todosLosMateriales(){return Optional.of(repositoryMatriales.findAll());}

    public Materiales buscarPorId(long id){ return repositoryMatriales.findById(id);}

    public Optional<List<Materiales>> buscarPorTipoDeMaterial(TipoMaterial tipo){return Optional.of(repositoryMatriales.findByTipoMaterial(tipo));}

    public List<Materiales> buscarPorValorMetroCuadrado(float valor){
        return repositoryMatriales.findAll().stream().filter(v -> {
            if (v.getValorMetroCuadrado()<=valor){
                return true;
            }
            return false;
        }).toList();
    }

    public Materiales buscarPorNombre(String Nombre){return repositoryMatriales.findByNombreMaterial(Nombre);}

    public void guardarMaterial(Materiales material){repositoryMatriales.save(material);}
    public void eliminarMaterial(Materiales material){repositoryMatriales.delete(material);}

    @Transactional
    public void actualizarMaterial(Materiales material){
        try{
            repositoryMatriales.saveAndFlush(material);
        }catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El material fue modificado por otro usuario"
            );
        }
    }
}
