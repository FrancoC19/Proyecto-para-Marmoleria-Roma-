package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RepositoryPiletas extends JpaRepository<Piletas,Long> {

    ArrayList<Piletas> findByModelo(String modelo);
    ArrayList<Piletas> findByMarca(String marca);
    ArrayList<Piletas> findByModeloAndMarca(String modelo, String marca);
}
