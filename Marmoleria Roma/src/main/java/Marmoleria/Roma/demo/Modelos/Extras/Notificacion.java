package Marmoleria.Roma.demo.Modelos.Extras;
import java.time.LocalDateTime;

public class Notificacion {
    private String mensaje;
    private LocalDateTime fecha;

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
}
