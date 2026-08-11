package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.DireccionInvalida;
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

    public Optional<List<Cliente>> buscarTodosClientes(){ return Optional.of(repoCliente.findAll()); }

    public Cliente buscarClientePorNombreYApellido(String nombre, String apellido){return repoCliente.findByNombreAndApellido(nombre,apellido);}

    public Cliente buscarClientePorTelefono(String telefono){return repoCliente.findByTelefono(telefono);}

    public Cliente buscarClientePorCorreo(String correo){return repoCliente.findByCorreo(correo);}

    public List<Direccion> buscarDireccionesCliente(Long dni) {
        Cliente cliente = buscarClientePorDNI(dni);
        return cliente.getDirecciones() != null ? cliente.getDirecciones() : new ArrayList<>();
    }

    public void agregarDireccionCliente(Long dni, Direccion direccion){
        Cliente cliente = buscarClientePorDNI(dni);
        cliente.agregarDireccion(direccion);
        guardarCliente(cliente);
    }

    public void eliminarDireccionCliente(Long dni, Direccion direccion){
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente.getDirecciones().contains(direccion)) {
            cliente.eliminarDireccion(direccion);
            guardarCliente(cliente);
        }
        else {
            throw new DireccionInvalida("La direccion ingresada no esta relacionada con este usuario");
        }
    }

    public List<Cliente> buscarClientes(String nombre, String apellido, String telefono, String correo, Long dni) {
        return repoCliente.buscarPorFiltros(nombre, apellido, telefono, correo, dni);
    }

    public void eliminarCliente(Long dni){
         repoCliente.deleteById(dni);
    }

}
