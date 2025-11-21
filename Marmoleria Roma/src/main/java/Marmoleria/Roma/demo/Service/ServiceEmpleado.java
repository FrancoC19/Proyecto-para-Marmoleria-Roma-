package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Personas.Empleado;
import Marmoleria.Roma.demo.Repository.RepositoryEmpleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEmpleado {
    @Autowired
    private RepositoryEmpleado repoEmpleado;

    public Empleado buscraEmpleadoPorNombre(String nombre) { return repoEmpleado.findByNombre(nombre); }

    public void guardarEmpleado(Empleado empleado) {  repoEmpleado.save(empleado); }

    public Empleado buscarEmpleadoPorDNI(Long dni) { return repoEmpleado.findByDNI(dni); }

    public Optional<List<Empleado>> ListaDeEmpleados(){return Optional.of(repoEmpleado.findAll());}

    public Empleado buscarPorCorreo (String correo) { return repoEmpleado.findByCorreo(correo); }

    public void EliminarEmpleado(long DNI) {  repoEmpleado.deleteById(DNI); }

}
