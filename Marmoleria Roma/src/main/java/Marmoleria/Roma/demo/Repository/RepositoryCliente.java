package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryCliente extends JpaRepository<Cliente,Long> {
    Cliente findByDNI(long dni);
    Cliente findByNombreAndApellido(String nombre, String apellido);
    Cliente findByCorreo(String correo);
    Cliente findByTelefono(String Telefono);
}
