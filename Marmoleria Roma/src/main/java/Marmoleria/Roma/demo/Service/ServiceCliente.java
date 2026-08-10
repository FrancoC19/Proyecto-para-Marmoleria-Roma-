package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Excepciones.DireccionInvalida;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import Marmoleria.Roma.demo.Repository.RepositoryCliente;
import jakarta.persistence.Access;
import jakarta.transaction.Transactional;
import jdk.jfr.TransitionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public void agregarDireccionCliente(Long dni, Direccion direccion){
        Cliente cliente = buscarClientePorDNI(dni);
        cliente.agregarDireccion(direccion);

        try {
            repoCliente.saveAndFlush(cliente);
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El cliente fue modificado por otro usuario"
            );
        }
    }

    @Transactional
    public void actualalizarCliente(Cliente cliente){
        try {
            repoCliente.saveAndFlush(cliente);
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El cliente fue modificado por otro usuario"
            );
        }
    }

    @Transactional
    public void eliminarDireccionCliente(Long dni, Direccion direccion){
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente.getDirecciones().contains(direccion)) {
            cliente.eliminarDireccion(direccion);

            try {
                repoCliente.saveAndFlush(cliente);
            } catch (OptimisticLockingFailureException e) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El cliente fue modificado por otro usuario"
                );
            }
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
