package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auditoria_documentaria")
public class AuditoriaDocumentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento")
    private Documento documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_version_documento")
    private VersionDocumento versionDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Users user;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "ip_address", nullable = false)
    private String ip_address;

    @Column(name = "user_agent")
    private String user_agent;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fecha_accion;
}
