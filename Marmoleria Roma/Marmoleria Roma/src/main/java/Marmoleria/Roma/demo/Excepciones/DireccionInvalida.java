package Marmoleria.Roma.demo.Excepciones;

public class DireccionInvalida extends RuntimeException {
    public DireccionInvalida(String message) {
        super(message);
    }
}
