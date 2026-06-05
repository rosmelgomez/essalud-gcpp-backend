package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitud_aprobacion_documento")
public class SolicitudAprobacionDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prioridad", nullable = false)
    private String prioridad;

    @Column(name = "comentario_solicitante")
    private String comentario_solicitante;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fecha_solicitud;

    @Column(name = "fecha_limite")
    private LocalDateTime fecha_limite;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_version_documento", nullable = false)
    private VersionDocumento versionDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_solicitante", nullable = false)
    private Users userSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_aprobador", nullable = false)
    private Users userAprobador;

    @OneToMany(mappedBy = "solicitudAprobacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ResolucionAprobacionDocumento> resoluciones;

    @OneToMany(mappedBy = "solicitud_aprobacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistorialAprobacionDocumento> historialAprobaciones;
}
