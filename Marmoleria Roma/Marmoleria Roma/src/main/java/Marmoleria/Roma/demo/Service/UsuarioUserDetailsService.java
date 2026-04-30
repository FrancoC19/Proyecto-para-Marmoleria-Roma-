package Marmoleria.Roma.demo.Service;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    @Autowired
    private RepositoryUsuario repoUsuario;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repoUsuario.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con correo: " + email));

        System.out.println("Usuario encontrado: " + usuario.getEmail());
        return usuario;
    }
}
