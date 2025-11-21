package Marmoleria.Roma.demo.Modelos.Personas;

import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Enumeradores.RolesEmpleado;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Empleado extends Persona {

    @NotNull(message = "El empleado debe poseer un rol...")
    @Enumerated(EnumType.STRING)
    private RolesEmpleado rolesEmpleado;



    public Empleado() {}

    public Empleado(Long DNI, String correo, String nombre, RolesEmpleado rolesEmpleado) {
        super(DNI, correo, nombre);
        this.rolesEmpleado = rolesEmpleado;
    }

    public RolesEmpleado getRolesEmpleado() {
        return rolesEmpleado;
    }

    public void setRolesEmpleado(RolesEmpleado rolesEmpleado) {
        this.rolesEmpleado = rolesEmpleado;
    }
}
