package Marmoleria.Roma.demo.Excepciones;

public class PedidoNoEncontrado extends RuntimeException {
    public PedidoNoEncontrado(String message) {
        super(message);
    }
}
