package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RepositoryMatriales extends JpaRepository<Materiales,Long>{
    Materiales findById(long id);
    ArrayList<Materiales> findByTipoMaterial(TipoMaterial tipoMaterial);
    ArrayList<Materiales> findByValorMetroCuadrado(Float valorMetroCuadrado);
    Materiales findByNombreMaterial(String nombreMaterial);
}
