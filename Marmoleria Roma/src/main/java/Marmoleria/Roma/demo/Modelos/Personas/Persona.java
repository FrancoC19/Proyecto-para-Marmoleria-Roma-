package Marmoleria.Roma.demo.Modelos.Personas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    @Id
    protected Long DNI;

    @NotBlank(message="El Cliente debe poseer un correo...")
    protected String correo;

    @NotBlank(message="Se debe poseer un nombre")
    protected String nombre;

    public Persona() {}

    public Persona(Long DNI, String correo, String nombre) {
        this.DNI = DNI;
        this.correo = correo;
        this.nombre = nombre;
    }

    public Long getDNI() {
        return DNI;
    }

    public void setDNI(Long DNI) {
        this.DNI = DNI;
    }

    public @NotBlank(message = "El Cliente debe poseer un correo...") String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "El Cliente debe poseer un correo...") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "Se debe poseer un nombre") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "Se debe poseer un nombre") String nombre) {
        this.nombre = nombre;
    }
}
