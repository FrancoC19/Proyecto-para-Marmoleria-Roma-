package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.IdNoEncontrado;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Repository.RepositoryUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Usuario buscarPorId(long id) { return repoUsuario.findById(id); }

    public Optional<Usuario> BuscarPorEmail(String email) { return repoUsuario.findByEmail(email); }

    public Optional<List<Usuario>> todosLosUsuarios(){return Optional.of(repoUsuario.findAll());}

    public void eliminarUsuario(Usuario usuario) {repoUsuario.delete(usuario);}

    @Transactional
    public void actualizarUsuario(Usuario usuario) {
        try{
            repoUsuario.saveAndFlush(usuario);
        }catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El usuario fue modificado por otro Administrador"
            );
        }
    }
}
