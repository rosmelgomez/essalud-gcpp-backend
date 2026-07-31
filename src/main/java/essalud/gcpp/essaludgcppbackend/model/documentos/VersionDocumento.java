package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
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
@Table(name = "version_documento")
public class VersionDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "extencion", nullable = false)
    private String extencion;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "size", nullable = false)
    private double size;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "date_delete")
    private LocalDateTime date_delete;

    @OneToOne(mappedBy = "versionDocumento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VersionDocumentoArchivo archivoVersion;

    @Column(name = "visible", nullable = false)
    private Boolean visible;

    /// relacion con usuario creador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_creation")
    private Usuario user_version_documento;

    /// relacion con documento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento")
    private Documento documento_version_documento;

    /// relacion con solicitud Aprobacion
    @OneToMany(mappedBy = "versionDocumento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolicitudAprobacionDocumento> solicitudesAprobacion;
}
