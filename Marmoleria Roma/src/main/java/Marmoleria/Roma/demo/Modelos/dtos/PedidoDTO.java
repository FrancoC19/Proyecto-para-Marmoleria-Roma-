package Marmoleria.Roma.demo.Modelos.dtos;

import Marmoleria.Roma.demo.Modelos.Extras.Direccion;

import java.time.LocalDate;

public class PedidoDTO {

    // Texto libre
    public String observaciones;

    // IDs de entidades relacionadas
    public Long clienteDni;
    public Long empleadoDni;
    public Long materialId;
    public Long piletaId;

    // Monto de seña
    public Integer senia;

    // Datos del pedido
    public String griferia;
    public String moldura;
    public LocalDate fechaEntrega;
    public LocalDate fechaEmision;
    public Float metrosCuadrados;
    public Float descuento;

    // Dirección embebida (igual al modelo)
    public Direccion direccion;
}
