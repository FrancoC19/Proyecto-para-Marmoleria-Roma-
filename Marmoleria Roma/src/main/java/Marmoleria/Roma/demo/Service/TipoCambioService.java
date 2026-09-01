package Marmoleria.Roma.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TipoCambioService {
    private final RestClient restClient = RestClient.create("https://dolarapi.com");

    private Float cotizacionCacheada = null;
    private Instant ultimaConsulta = null;
    private static final long MINUTOS_CACHE = 15;

    // Registro para mapear la respuesta JSON de dolarapi.com
    private record DolarResponse(String moneda, String casa, String nombre,
                                 Float compra, Float venta, String fechaActualizacion) {}

    public synchronized Float obtenerCotizacionDolar() {
        boolean cacheValido = ultimaConsulta != null
                && ChronoUnit.MINUTES.between(ultimaConsulta, Instant.now()) < MINUTOS_CACHE;

        if (cacheValido && cotizacionCacheada != null) {
            return cotizacionCacheada;
        }

        try {
            DolarResponse respuesta = restClient.get()
                    .uri("/v1/dolares/blue")
                    .retrieve()
                    .body(DolarResponse.class);

            if (respuesta != null && respuesta.venta() != null) {
                cotizacionCacheada = respuesta.venta();
                ultimaConsulta = Instant.now();
                return cotizacionCacheada;
            }
        } catch (Exception e) {
            System.err.println("No se pudo obtener la cotización del dólar: " + e.getMessage());
        }

        // Si falla la API y ya teníamos una cotización previa cacheada, la seguimos usando
        if (cotizacionCacheada != null) {
            return cotizacionCacheada;
        }

        throw new IllegalStateException("No se pudo obtener la cotización del dólar y no hay valor en caché");
    }
}
