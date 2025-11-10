package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryCliente extends JpaRepository<Cliente,Long> {
    Cliente findByDNI(long dni);
    Cliente findByNombreAndApellido(String nombre, String apellido);
    Cliente findByCorreo(String correo);
    Cliente findByTelefono(String Telefono);

    @Query("SELECT c FROM Cliente c " +
            "WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:apellido IS NULL OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) " +
            "AND (:telefono IS NULL OR c.telefono = :telefono) " +
            "AND (:correo IS NULL OR LOWER(c.correo) LIKE LOWER(CONCAT('%', :correo, '%'))) " +
            "AND (:dni IS NULL OR c.DNI = :dni)")
    List<Cliente> buscarPorFiltros(@Param("nombre") String nombre,
                                   @Param("apellido") String apellido,
                                   @Param("telefono") String telefono,
                                   @Param("correo") String correo,
                                   @Param("dni") Long dni);
}
