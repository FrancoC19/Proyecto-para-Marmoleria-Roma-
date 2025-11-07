package Marmoleria.Roma.demo.Initializer;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Repository.RepositoryUsuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    private final RepositoryUsuario repositoryUsuario;

    public DataInitializer(RepositoryUsuario repositoryUsuario) {
        this.repositoryUsuario = repositoryUsuario;
    }

    @Bean
    public CommandLineRunner crearAdminAlIniciar() {
        return args -> {
            String correoAdmin = "admin@marmoleria.com";

            // Buscar si ya existe un usuario con ese correo
            Usuario adminExistente = repositoryUsuario.findByEmail(correoAdmin).orElse(null);

            if (adminExistente == null) {
                Usuario admin = new Usuario(
                        TipoUsuario.ADMINISTRADOR,
                        correoAdmin,
                        "admin123"
                );

                repositoryUsuario.save(admin);
                System.out.println("✅ Usuario administrador creado correctamente.");
            } else {
                System.out.println("ℹ️ El usuario administrador ya existe, no se creó uno nuevo.");
            }
        };
    }
}
