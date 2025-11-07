package Marmoleria.Roma.demo.Excepciones;

public class DireccionNoEncontrada extends RuntimeException {
    public DireccionNoEncontrada(String message) {
        super(message);
    }
}
