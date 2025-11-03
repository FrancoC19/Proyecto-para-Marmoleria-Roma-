package Marmoleria.Roma.demo.Excepciones;

import java.util.function.Supplier;

public class PiletaNoEncontrada extends RuntimeException implements Supplier<X> {
    public PiletaNoEncontrada(String message) {
        super(message);
    }
}
