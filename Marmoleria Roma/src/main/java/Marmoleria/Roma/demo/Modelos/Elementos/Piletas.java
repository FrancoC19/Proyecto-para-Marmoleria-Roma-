package Marmoleria.Roma.demo.Modelos.Elementos;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Piletas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pileta")
    @SequenceGenerator(name = "id_pileta", sequenceName = "id_pileta", allocationSize = 1)
    private Long id_pileta;

    @NotBlank(message = "La pileta debe poseer una marca...")
    private String marca;

    @NotBlank(message = "La pileta debe poseer un modelo...")
    private String modelo;


    @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo")
    private Float largo;


    @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo")
    private Float ancho;

    @DecimalMin(value="1.0",message ="La profundidad no puede ser negativa")
    private float profundidad;

    @DecimalMin(value="0",message = "El valor de la pileta no pueed ser menor a 0")
    private Float valor;

    @NotNull(message = "EL valor no puede ser negativo")
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NotNull(message = "EL valor no puede ser negativo") int cantidad) {
        this.cantidad = cantidad;
    }

    @NotNull( message = "EL valor no puede ser negativo")
    private int cantidad;

    public Piletas() {}

    public Long getId() {
        return id_pileta;
    }



    public @NotBlank(message = "La pileta debe poseer una marca...") String getMarca() {
        return marca;
    }

    public void setMarca(@NotBlank(message = "La pileta debe poseer una marca...") String marca) {
        this.marca = marca;
    }

    public @NotBlank(message = "La pileta debe poseer un modelo...") String getModelo() {
        return modelo;
    }

    public void setModelo(@NotBlank(message = "La pileta debe poseer un modelo...") String modelo) {
        modelo = modelo;
    }

    public  @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo") Float getLargo() {
        return largo;
    }

    public void setLargo( @DecimalMin(value = "1.0", message = "El largo no puede ser cero o negativo") Float largo) {
        this.largo = largo;
    }

    public  @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo") Float getAncho() {
        return ancho;
    }

    public void setAncho( @DecimalMin(value = "1.0", message = "EL ancho no puede ser cero o negativo") Float ancho) {
        this.ancho = ancho;
    }

    public @DecimalMin(value = "0", message = "El valor de la pileta no pueed ser menor a 0") Float getValor() {
        return valor;
    }

    public void setValor(@DecimalMin(value = "0", message = "El valor de la pileta no pueed ser menor a 0") Float valor) {
        this.valor = valor;
    }

    @DecimalMin(value = "1.0", message = "La profundidad no puede ser negativa")
    public float getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(@DecimalMin(value = "1.0", message = "La profundidad no puede ser negativa") float profundidad) {
        this.profundidad = profundidad;
    }
}
