package Marmoleria.Roma.demo.Modelos.Elementos;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Piletas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pileta")
    @SequenceGenerator(name = "id_pileta", sequenceName = "id_pileta", allocationSize = 1)
    private Long Id;

    @NotBlank(message = "La pileta debe poseer una marca...")
    private String Marca;

    @NotBlank(message = "La pileta debe poseer un modelo...")
    private String Modelo;


    @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo")
    private Float largo;


    @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo")
    private Float Ancho;

    public Piletas() {}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public @NotBlank(message = "La pileta debe poseer una marca...") String getMarca() {
        return Marca;
    }

    public void setMarca(@NotBlank(message = "La pileta debe poseer una marca...") String marca) {
        Marca = marca;
    }

    public @NotBlank(message = "La pileta debe poseer un modelo...") String getModelo() {
        return Modelo;
    }

    public void setModelo(@NotBlank(message = "La pileta debe poseer un modelo...") String modelo) {
        Modelo = modelo;
    }

    public  @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo") Float getLargo() {
        return largo;
    }

    public void setLargo( @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo") Float largo) {
        this.largo = largo;
    }

    public  @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo") Float getAncho() {
        return Ancho;
    }

    public void setAncho( @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo") Float ancho) {
        Ancho = ancho;
    }
}
