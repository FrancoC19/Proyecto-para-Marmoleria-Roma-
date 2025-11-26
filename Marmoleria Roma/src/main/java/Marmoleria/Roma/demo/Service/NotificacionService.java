package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Extras.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private ServicePedidos servicePedidos;

    private final List<Notificacion> notificaciones = new ArrayList<>();

    @Scheduled(cron = "0 0 9,15 * * *")
    public void notificarPedidosProximosProgramado() {
        generarNotificaciones();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void notificarAlIniciar() {
        generarNotificaciones();
    }

    public void notificacarPorLlamada(){
        generarNotificaciones();
    }

    private void generarNotificaciones() {
        notificaciones.clear(); // 🧹 limpia las notificaciones previas

        servicePedidos.pedidosProximosAVencer(3).ifPresent(pedidos -> {
            pedidos.forEach(p -> {
                String mensaje = "⚠️ Pedido " + p.getIdPedido() +
                        " del cliente " + p.getCliente().getNombre() +
                        " está próximo a vencer el " + p.getFechaEntrega();
                notificaciones.add(new Notificacion(mensaje));
            });
        });
    }

    public List<Notificacion> obtenerNotificaciones() {
        return notificaciones;
    }
}
