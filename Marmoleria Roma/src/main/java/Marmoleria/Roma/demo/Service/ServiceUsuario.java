package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.IdNoEncontrado;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceUsuario {
    @Autowired
    private RepositoryUsuario repoUsuario;

    public void guardarUsuario(Usuario usuario) {
        repoUsuario.save(usuario);
    }

    public Optional<List<Usuario>> BuscarPorTipoDeUsuario(TipoUsuario tipo) {return Optional.of(repoUsuario.findByTipoUsuario(tipo)); }

    public Optional<Usuario> BuscarPorId(long id) { return Optional.of(repoUsuario.findById(id)); }

    public Optional<Usuario> BuscarPorEmail(String email) { return repoUsuario.findByEmail(email); }

    public Optional<List<Usuario>> todosLosUsuarios(){return Optional.of(repoUsuario.findAll());}
}
