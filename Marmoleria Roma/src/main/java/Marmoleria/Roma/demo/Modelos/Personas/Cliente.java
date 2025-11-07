package Marmoleria.Roma.demo.Modelos.Personas;

import Marmoleria.Roma.demo.Excepciones.DireccionInvalida;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Cliente")
    @SequenceGenerator(name = "id_Cliente", sequenceName = "id_Cliente", allocationSize = 1)
    protected long id_Cliente;

    @NotBlank(message="El Cliente debe poseer un apellido...")
    private String Apellido;

    @NotNull(message = "El cliente debe tener un telefono...")
    @Size(min=10,max = 10, message = "El numero debe contac con 10 caracteres, 3 del area y 7 identificativo")
    @Pattern(regexp = "\\d{10}", message = "El teléfono solo puede contener números")
    private String telefono;

    @ElementCollection
    @CollectionTable(name = "cliente_direcciones", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<Direccion> direcciones;

    public Cliente(){}

    public Cliente(Long DNI, String correo, String nombre, String apellido, String telefono){
        super(DNI, correo, nombre);
        this.Apellido = apellido;
        this.telefono = telefono;
        this.direcciones = new ArrayList<>();
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

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void agregarDireccion(Direccion direccion) {
        if (direccion == null) {
            throw new DireccionInvalida("La dirección no puede ser nula");
        }

        if (direcciones == null) {
            direcciones = new ArrayList<>();
        }

        if (direcciones.contains(direccion)) {
            throw new DireccionInvalida("La dirección ya está registrada para este cliente");
        }

        direcciones.add(direccion);
    }

    public void eliminarDireccion(Direccion direccion) {
        if (direccion == null) {
            throw new DireccionInvalida("La dirección a eliminar no puede ser nula");
        }

        if (direcciones == null || direcciones.isEmpty()) {
            throw new DireccionInvalida("El cliente no tiene direcciones registradas");
        }

        if (!direcciones.remove(direccion)) {
            throw new DireccionInvalida("La dirección no pertenece a este cliente");
        }
    }
}
