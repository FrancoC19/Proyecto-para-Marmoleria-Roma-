package Marmoleria.Roma.demo.Modelos.Extras;

import jakarta.validation.constraints.NotBlank;

public class Direccion {

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
