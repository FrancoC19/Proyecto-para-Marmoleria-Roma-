package Marmoleria.Roma.demo.Controller;
import Marmoleria.Roma.demo.Service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Email Controller", description="Controlador de email")
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation (description = "Envia un correo personalizado, definiendo para quien es, con que asunto y conteniendo un mensaje")
    @PreAuthorize("hasAnyRole('USUARIO','ADMINISTRADOR')")
    @PostMapping("/enviar")
    public String enviarCorreo(
            @RequestParam String para,
            @RequestParam String asunto,
            @RequestParam String mensaje) {
        emailService.enviarCorreoSimple(para, asunto, mensaje);
        return "Correo enviado a " + para;
    }
}

