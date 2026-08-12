package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import Marmoleria.Roma.demo.Repository.RepositoryPiletas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicePiletas {
    @Autowired
    private RepositoryPiletas repoPiletas;

    public void guardarPileta(Piletas pileta){ repoPiletas.save(pileta);}

    public Piletas buscarPorId(Long id){
        return repoPiletas.findById(id).orElse(null);
    }

    public Optional<List<Piletas>> buscarPorModelo(String modelo){ return Optional.of(repoPiletas.findByModelo(modelo));}

    public Optional<List<Piletas>> buscarPorMarca(String marca){return Optional.of(repoPiletas.findByMarca(marca));}

    public Optional<List<Piletas>> buscarModeloYMarca (String marca,String modelo){return Optional.of(repoPiletas.findByModeloAndMarca( modelo,marca));}

    public Optional<List<Piletas>> todasLasPiletas (){ return Optional.of(repoPiletas.findAll());}

    public void eliminarPileta (Piletas pileta){repoPiletas.delete(pileta);}

}
