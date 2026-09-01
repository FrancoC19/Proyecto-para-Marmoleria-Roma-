package Marmoleria.Roma.demo.Modelos.dtos;

import java.util.List;

public class CotizacionDTO {
    public Long materialId;
    public Long piletaId; // opcional, null = sin pileta
    public Float metrosCuadrados;

    // Mano de obra / adicionales a cotizar (mismo formato que en PedidoDTO)
    public List<PedidoDTO.ItemAdicionalDTO> itemsAdicionales;
}
