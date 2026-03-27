package essalud.gcpp.essaludgcppbackend.model;
import java.time.LocalDateTime;
import java.util.List;

import essalud.gcpp.essaludgcppbackend.model.encuesta.Encuesta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "codcas")
    private String codcas;

    @Column(name = "code_red")
    private String code_red;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    /// relacion con usuario_rol
    @OneToMany(mappedBy = "usuario_rol" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRol> usuarios_roles;

    /// relacion con logs
    @OneToMany(mappedBy = "user_log", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Log> logs;

    /// relacion con encuesta
    @OneToMany(mappedBy = "user_encuesta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;

    /// relacion con solicitud acceso
    @OneToMany(mappedBy = "user_solicitud", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolicitudAcceso> solicitudes;

}