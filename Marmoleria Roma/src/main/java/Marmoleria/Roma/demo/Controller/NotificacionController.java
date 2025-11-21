package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Modelos.Extras.Notificacion;
import Marmoleria.Roma.demo.Service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/todas")
    public List<Notificacion> getNotificaciones() {
        return notificacionService.obtenerNotificaciones();
    }

    //En el front, consultar para ejecutar esta cada cierto tiempo, a las 9 y a las 15
}
