package essalud.gcpp.essaludgcppbackend.model.usuario;
import java.time.LocalDateTime;
import java.util.List;

import essalud.gcpp.essaludgcppbackend.model.documentos.*;
import essalud.gcpp.essaludgcppbackend.model.encuesta.Encuesta;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "dni")
    private String dni;

    @Column(name = "codcas")
    private String codcas;

    @Column(name = "codred")
    private String codred;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update")
    private LocalDateTime date_update;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    @Column(name = "first_login", nullable = false)
    private Boolean first_Login = true;

    /// relacion con usuario_rol
    @OneToMany(mappedBy = "usuario_rol" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UsuarioRol> usuarios_roles;

    /// relacion con logs
    @OneToMany(mappedBy = "user_log", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Log> logs;

    /// relacion con encuesta
    @OneToMany(mappedBy = "user_encuesta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;

    /// relacion con solicitud acceso
    @OneToMany(mappedBy = "user_solicitud", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolicitudAcceso> solicitudes;

    ///relacion con documento
    @OneToMany(mappedBy = "user_documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Documento> documentos ;

    /// relacion con version documento
    @OneToMany(mappedBy = "user_version_documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VersionDocumento> version_documentos;

    ///relacion con solicitud de aprobacion
    @OneToMany(mappedBy = "userAprobador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolicitudAprobacionDocumento> flujo_aprobaciones;

    /// relacion con solitudes enviadas
    @OneToMany(mappedBy = "userSolicitante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolicitudAprobacionDocumento> solicitudesEnviadas;

    ///relacion con aprobacionDocumento
    @OneToMany(mappedBy = "userRevisor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ResolucionAprobacionDocumento> resolucionesRealizadas;
}