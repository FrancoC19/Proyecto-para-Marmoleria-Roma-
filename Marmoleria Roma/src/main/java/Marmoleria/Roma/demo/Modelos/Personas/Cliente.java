package Marmoleria.Roma.demo.Modelos.Personas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Cliente extends Persona {

    @NotBlank(message="El Cliente debe poseer un apellido...")
    protected String Apellido;

    @NotNull(message = "El cliente debe tener un telefono...")
    @Size(min=10,max = 10, message = "El numero debe contac con 10 caracteres, 3 del area y 7 identificativo")
    @Pattern(regexp = "\\d{10}", message = "El teléfono solo puede contener números")
    protected String telefono;

    public Cliente(){}

    public Cliente(Long DNI, String correo, String nombre, String apellido, String telefono){
        super(DNI, correo, nombre);
        this.Apellido = apellido;
        this.telefono = telefono;
    }

    public @NotBlank(message = "El Cliente debe poseer un apellido...") String getApellido() {
        return Apellido;
    }

    public void setApellido(@NotBlank(message = "El Cliente debe poseer un apellido...") String apellido) {
        Apellido = apellido;
    }

    public @NotNull(message = "El cliente debe tener un telefono...") @Size(min = 10, max = 10, message = "El numero debe contac con 10 caracteres, 3 del area y 7 identificativo") @Pattern(regexp = "\\d{10}", message = "El teléfono solo puede contener números") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotNull(message = "El cliente debe tener un telefono...") @Size(min = 10, max = 10, message = "El numero debe contac con 10 caracteres, 3 del area y 7 identificativo") @Pattern(regexp = "\\d{10}", message = "El teléfono solo puede contener números") String telefono) {
        this.telefono = telefono;
    }
}
