package Marmoleria.Roma.demo.Repository;

import Marmoleria.Roma.demo.Modelos.Extras.ItemAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryItemAdicional extends JpaRepository<ItemAdicional, Long> {
    List<ItemAdicional> findByActivoTrue();
}
