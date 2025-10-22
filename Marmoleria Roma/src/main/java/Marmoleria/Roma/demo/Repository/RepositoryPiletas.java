package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Elementos.Piletas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;

public interface RepositoryPiletas extends JpaRepository<Piletas,Long> {
    Piletas findById(long id);
    ArrayList<Piletas> findByModelo(String modelo);
    ArrayList<Piletas> findByMarca(String marca);
    ArrayList<Piletas> findByModeloAndMarca(String modelo, String marca);
}
