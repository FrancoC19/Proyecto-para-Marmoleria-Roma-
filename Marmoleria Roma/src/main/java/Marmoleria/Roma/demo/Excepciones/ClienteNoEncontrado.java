package Marmoleria.Roma.demo.Excepciones;

public class ClienteNoEncontrado extends RuntimeException {
    public ClienteNoEncontrado(String message) {
        super(message);
    }
}
