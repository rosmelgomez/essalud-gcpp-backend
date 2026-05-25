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

    @Column(name = "version" , nullable = false)
    private int version;

    @Column(name = "size" , nullable = false)
    private double size;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Lob
    @Column(name = "archivo")
    private byte[] archivo;

    @Column(name = "file_key", length = 512)
    private String fileKey;

    @Column(name = "storage_provider", length = 50)
    private String storageProvider = "DATABASE";

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_user_id")
    private Users deletedBy;

    /// relacion con usuario creador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_creation")
    private Users user_version_documento;

    /// relacion con documento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento")
    private Documento documento_version_documento;

    /// relacion con flujo Aprobacion
    @OneToMany(mappedBy = "version_documento_aprobacion" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlujoAprobacionDocumento> flujo_aprobaciones;
}
