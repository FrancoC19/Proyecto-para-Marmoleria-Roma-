package Marmoleria.Roma.demo.Modelos.Requests;

public class LoginRequest {
    private String email;
    private String contrasenia;

    public String getCorreo() {
        return email;
    }
    public void setCorreo(String email) {
        this.email = email;
    }
    public String getPassword() {
        return contrasenia;
    }
    public void setPassword(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
