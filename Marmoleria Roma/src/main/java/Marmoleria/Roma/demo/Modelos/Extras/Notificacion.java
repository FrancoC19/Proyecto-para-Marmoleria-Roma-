package Marmoleria.Roma.demo.Modelos.Extras;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Notificacion {
    private String mensaje;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    public Notificacion() {}

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public String getMensaje() { return this.mensaje; }
    public LocalDateTime getFecha() { return this.fecha; }
}
