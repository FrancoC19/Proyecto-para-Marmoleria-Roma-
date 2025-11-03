package Marmoleria.Roma.demo.Excepciones;

public class EmpleadoInexistente extends RuntimeException {
    public EmpleadoInexistente(String message) {
        super(message);
    }
}
