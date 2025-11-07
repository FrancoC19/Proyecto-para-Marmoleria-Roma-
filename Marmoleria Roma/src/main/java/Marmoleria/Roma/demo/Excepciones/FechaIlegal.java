package Marmoleria.Roma.demo.Excepciones;

public class FechaIlegal extends RuntimeException {
    public FechaIlegal(String message) {
        super(message);
    }
}
