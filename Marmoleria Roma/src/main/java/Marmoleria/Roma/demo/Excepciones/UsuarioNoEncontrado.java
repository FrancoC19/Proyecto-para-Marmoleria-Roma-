package Marmoleria.Roma.demo.Excepciones;

public class UsuarioNoEncontrado extends RuntimeException {
  public UsuarioNoEncontrado(String message) {
    super(message);
  }
}
