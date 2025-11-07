package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Repository.RepositoryCliente;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceCliente {
    @Autowired
    private RepositoryCliente repoCliente;

    public void guardarCliente(Cliente cliente) {repoCliente.save(cliente);}

    public Cliente buscarClientePorDNI(Long dni){  return repoCliente.findByDNI(dni); }

    public Optional<List<Cliente>> buscarClientes(){ return Optional.of(repoCliente.findAll()); }

    public Cliente buscarClientePorNombreYApellido(String nombre, String apellido){return repoCliente.findByNombreAndApellido(nombre,apellido);}

    public Cliente buscarClientePorTelefono(String telefono){return repoCliente.findByTelefono(telefono);}

    public Cliente buscarClientePorCorreo(String correo){return repoCliente.findByCorreo(correo);}

    public List<Direccion> buscarDireccionesCliente(Long dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        return cliente.getDirecciones() != null ? cliente.getDirecciones() : new ArrayList<>();
    }


}
