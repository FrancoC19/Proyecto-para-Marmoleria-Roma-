package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RepositoryMatriales extends JpaRepository<Materiales,Long>{
    Materiales findById(long id);
    ArrayList<Materiales> findByTipoMaterial(TipoMaterial tipoMaterial);
    Materiales findByNombreMaterial(String nombreMaterial);
    List<Materiales> findByGrupoPrecio(GrupoPrecio grupoPrecio);
}
