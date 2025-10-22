package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Materiales;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RepositoryMatriales extends JpaRepository<Materiales,Long>{
    Materiales findById(long id);
    ArrayList<Materiales> findByTipoMaterial(TipoMaterial tipoMaterial);
    ArrayList<Materiales> findByValorMetroCuadrado(Float valorMetroCuadrado);
    Materiales findByNombreMaterial(String nombreMaterial);
}
