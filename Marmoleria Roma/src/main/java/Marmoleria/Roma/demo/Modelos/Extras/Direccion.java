package Marmoleria.Roma.demo.Modelos.Extras;

import Marmoleria.Roma.demo.Modelos.Personas.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class Direccion {

    @NotBlank(message="La direccion debe poseer una calle...")
    private String calle;

    @NotBlank(message="La direccion debe poseer un numero...")
    private String numero;

    @NotBlank(message="La direccion debe poseer una localidad...")
    private String localidad;

    public Direccion() {}

    public @NotBlank(message = "La direccion debe poseer una calle...") String getCalle() {
        return calle;
    }

    public void setCalle(@NotBlank(message = "La direccion debe poseer una calle...") String calle) {
        this.calle = calle;
    }

    public @NotBlank(message = "La direccion debe poseer un numero...") String getNumero() {
        return numero;
    }

    public void setNumero(@NotBlank(message = "La direccion debe poseer un numero...") String numero) {
        this.numero = numero;
    }

    public @NotBlank(message = "La direccion debe poseer una localidad...") String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(@NotBlank(message = "La direccion debe poseer una localidad...") String localidad) {
        this.localidad = localidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direccion)) return false;
        Direccion that = (Direccion) o;
        return calle.equalsIgnoreCase(that.calle) &&
                numero.equalsIgnoreCase(that.numero) &&
                localidad.equalsIgnoreCase(that.localidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle.toLowerCase(), numero.toLowerCase(), localidad.toLowerCase());
    }

    @Override
    public String toString() {
        return calle + " " + numero + ", " + localidad;
    }
}
