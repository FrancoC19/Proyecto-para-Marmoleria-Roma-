package Marmoleria.Roma.demo.Modelos.Extras;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Direccion {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message="La direccion debe poseer una calle...")
    private String Calle;

    @NotBlank(message="La direccion debe poseer un numero...")
    private String Numero;

    @NotBlank(message="La direccion debe poseer una localidad...")
    private String Localidad;

    public Direccion() {}

    public @NotBlank(message = "La direccion debe poseer una calle...") String getCalle() {
        return Calle;
    }

    public void setCalle(@NotBlank(message = "La direccion debe poseer una calle...") String calle) {
        Calle = calle;
    }

    public @NotBlank(message = "La direccion debe poseer un numero...") String getNumero() {
        return Numero;
    }

    public void setNumero(@NotBlank(message = "La direccion debe poseer un numero...") String numero) {
        Numero = numero;
    }

    public @NotBlank(message = "La direccion debe poseer una localidad...") String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(@NotBlank(message = "La direccion debe poseer una localidad...") String localidad) {
        Localidad = localidad;
    }
}
