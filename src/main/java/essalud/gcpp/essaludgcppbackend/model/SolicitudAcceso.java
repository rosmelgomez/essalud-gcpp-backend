package essalud.gcpp.essaludgcppbackend.model;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "solicitud_acceso")
public class SolicitudAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rol_solicitud", nullable = false)
    private String rol_solicitud;

    @Column(name = "code_red_solicitud")
    private String code_red_solicitud;

    @Column(name = "codcas_solicitud")
    private String codcas_solicitud;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "status", nullable = false)
    private String status;

    /// relacion con usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Users user_solicitud;

}
