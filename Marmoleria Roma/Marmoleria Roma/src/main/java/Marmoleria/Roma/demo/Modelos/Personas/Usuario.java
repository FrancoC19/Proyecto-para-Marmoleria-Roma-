package Marmoleria.Roma.demo.Modelos.Personas;

import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Usuario")
    @SequenceGenerator(name = "id_Usuario", sequenceName = "id_Usuario", allocationSize = 1)
    private Long id;

    @NotNull(message = "El empleado debe poseer un rol...")
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Email(message = "Debe ingresar un correo válido")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "El usuario debe poseer un correo...")
    private String email;

    @NotBlank(message = "El usuario deve poseer una contraseña...")
    private String contra;


    public Usuario(){}

    public Usuario(TipoUsuario tipoUsuario, String Email, String contra){
        this.tipoUsuario = tipoUsuario;
        this.email = Email;
        this.contra = contra;
        encriptarPassword();
    }

    /*---------------------------------elementos de JWT----------------------------------------------*/
    // Encriptar password antes de persistir o actualizar
    @PrePersist
    @PreUpdate
    private void encriptarPassword() {
        if (contra != null && !contra.startsWith("$2a$")) {
            this.contra = new BCryptPasswordEncoder().encode(this.contra);
        }
    }

    //metodos para userdetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertimos el rol en GrantedAuthority para Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    @Override
    public String getUsername() {
        // Usamos el correo como username
        return this.email;
    }

    @Override
    public String getPassword() {
        return contra;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    /*--------------------------------fin elementos jwt --------------------------------------------------- */

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public @Email(message = "Debe ingresar un correo válido") @NotBlank(message = "El usuario debe poseer un correo...") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Debe ingresar un correo válido") @NotBlank(message = "El usuario debe poseer un correo...") String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
