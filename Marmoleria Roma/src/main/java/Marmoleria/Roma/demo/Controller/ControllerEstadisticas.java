package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Repository.RepositoryPedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Estadisticas")
public class ControllerEstadisticas {
    @Autowired
    private RepositoryPedidos repositoryPedidos;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/MetrosPorMaterial")
    public ResponseEntity<List<Map<String, Object>>> getMetrosPorMaterial(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        // Usamos el método que ya existe en el repo
        var pedidos = repositoryPedidos.findByFechaEmisionBetween(desde, hasta);

        // Agrupamos en Java por nombre de material
        Map<String, double[]> agrupado = new LinkedHashMap<>();
        for (var p : pedidos) {
            String mat = p.getMaterial().getNombreMaterial();
            agrupado.putIfAbsent(mat, new double[]{0, 0});
            agrupado.get(mat)[0] += p.getMetrosCuadrados(); // metros
            agrupado.get(mat)[1]++;                          // cantidad pedidos
        }

        List<Map<String, Object>> respuesta = new ArrayList<>();
        agrupado.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue()[0], a.getValue()[0]))
                .forEach(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("material", e.getKey());
                    m.put("metrosCuadrados", Math.round(e.getValue()[0] * 100.0) / 100.0);
                    m.put("cantidadPedidos", (int) e.getValue()[1]);
                    respuesta.add(m);
                });

        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    @GetMapping("/PedidosPorEstado")
    public ResponseEntity<List<Map<String, Object>>> getPedidosPorEstado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        var pedidos = repositoryPedidos.findByFechaEmisionBetween(desde, hasta);

        Map<String, Integer> agrupado = new LinkedHashMap<>();
        for (var p : pedidos) {
            agrupado.merge(p.getEstado(), 1, Integer::sum);
        }

        List<Map<String, Object>> respuesta = new ArrayList<>();
        agrupado.forEach((estado, cantidad) -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("estado", estado);
            m.put("cantidad", cantidad);
            respuesta.add(m);
        });

        return ResponseEntity.ok(respuesta);
    }
}
