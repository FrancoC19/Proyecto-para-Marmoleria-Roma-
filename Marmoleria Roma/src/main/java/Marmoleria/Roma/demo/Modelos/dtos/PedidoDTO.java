package Marmoleria.Roma.demo.Modelos.dtos;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Extras.Direccion;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    // Texto libre
    public String observaciones;

    // IDs de entidades relacionadas
    public Long clienteDni;
    public Long empleadoDni;
    public Long materialId;
    public Long piletaId; // null = mesada ciega, sin pileta

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

    // Mano de obra / adicionales elegidos para este pedido
    public List<ItemAdicionalDTO> itemsAdicionales;

    public static class ItemAdicionalDTO {
        public Long itemAdicionalId;      // null si es un caso manual/puntual
        public Float cantidad;            // metros lineales, m², o cantidad, según corresponda

        // Solo se usan si itemAdicionalId es null (caso puntual)
        public String descripcionManual;
        public Float precioManual;
        public Moneda monedaManual;
    }
}
