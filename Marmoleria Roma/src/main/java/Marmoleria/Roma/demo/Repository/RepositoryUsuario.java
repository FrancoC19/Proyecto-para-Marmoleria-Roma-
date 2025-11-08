package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuario,Long> {
    Usuario findById(long id);
    ArrayList<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    Optional<Usuario> findByEmail(String email);
}
