package Marmoleria.Roma.demo.Modelos.Elementos;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Piletas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pileta")
    @SequenceGenerator(name = "id_pileta", sequenceName = "id_pileta", allocationSize = 1)
    private Long id_pileta;

    @NotBlank(message = "La pileta debe poseer una marca...")
    private String Marca;

    @NotBlank(message = "La pileta debe poseer un modelo...")
    private String Modelo;


    @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo")
    private Float largo;


    @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo")
    private Float Ancho;

    @DecimalMin(value="0",message = "El valor de la pileta no pueed ser menor a 0")
    private Float valor;

    public Piletas() {}

    public Long getId() {
        return id_pileta;
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

    public @DecimalMin(value = "0", message = "El valor de la pileta no pueed ser menor a 0") Float getValor() {
        return valor;
    }

    public void setValor(@DecimalMin(value = "0", message = "El valor de la pileta no pueed ser menor a 0") Float valor) {
        this.valor = valor;
    }
}
