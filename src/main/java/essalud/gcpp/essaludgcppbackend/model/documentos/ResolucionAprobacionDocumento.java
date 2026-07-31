package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resolucion_aprobacion_documento")
public class ResolucionAprobacionDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_aprobacion", nullable = false)
    private SolicitudAprobacionDocumento solicitudAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_revisor", nullable = false)
    private Usuario userRevisor;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "comentario_aprobador")
    private String comentario_aprobador;

    @Column(name = "fecha_resolucion", nullable = false)
    private LocalDateTime fecha_resolucion;
}
