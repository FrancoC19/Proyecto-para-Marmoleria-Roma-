package Marmoleria.Roma.demo.Repository;

import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import Marmoleria.Roma.demo.Modelos.Extras.GrupoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryGrupoPrecio extends JpaRepository<GrupoPrecio, Long> {
        List<GrupoPrecio> findByTipoMaterialAndActivoTrue(TipoMaterial tipoMaterial);
        List<GrupoPrecio> findByTipoMaterial(TipoMaterial tipoMaterial);

}
