package essalud.gcpp.essaludgcppbackend.model.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resolucion_solicitud_acceso")
public class ResolucionSolicitudAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudAcceso solicitudAcceso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_revisor", nullable = false)
    private Usuario userRevisor;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "comentario_revisor", length = 500)
    private String comentarioRevisor;

    @Column(name = "date_resolution", nullable = false)
    private LocalDateTime dateResolution;
}
