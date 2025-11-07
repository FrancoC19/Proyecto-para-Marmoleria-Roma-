package Marmoleria.Roma.demo.Controller;

import Marmoleria.Roma.demo.Service.ServicePedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Pedidos")
public class ControllerPedidos {
    @Autowired
    ServicePedidos servicePedido;


}
