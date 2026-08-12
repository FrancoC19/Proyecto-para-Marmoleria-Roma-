package Marmoleria.Roma.demo.Repository;
import Marmoleria.Roma.demo.Modelos.Enumeradores.RolesEmpleado;
import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RepositoryEmpleado extends JpaRepository<Empleado,Long> {
    Empleado findByNombre(String nombre);
    Empleado findByDNI(long dni);
    //ArrayList<Empleado> findByRolesEmpleado(RolesEmpleado rolesEmpleado);
    Empleado findByCorreo(String correo);
}
