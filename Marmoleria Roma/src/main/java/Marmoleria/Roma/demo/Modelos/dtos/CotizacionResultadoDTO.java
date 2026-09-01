package Marmoleria.Roma.demo.Modelos.dtos;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;

public class CotizacionResultadoDTO {
    public Moneda moneda;
    public float totalMaterial;
    public float totalPileta;
    public float totalManoDeObra;
    public float total;

    public CotizacionResultadoDTO(Moneda moneda, float totalMaterial, float totalPileta, float totalManoDeObra) {
        this.moneda = moneda;
        this.totalMaterial = totalMaterial;
        this.totalPileta = totalPileta;
        this.totalManoDeObra = totalManoDeObra;
        this.total = totalMaterial + totalPileta + totalManoDeObra;
    }
}
