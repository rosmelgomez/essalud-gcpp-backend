package essalud.gcpp.essaludgcppbackend.model.documentos;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "version_documento_archivo")
public class VersionDocumentoArchivo {

    @Id
    @Column(name = "id_version_documento")
    private Integer idVersionDocumento;

    @Lob
    @Column(name = "archivo", nullable = false)
    private byte[] archivo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_version_documento")
    private VersionDocumento versionDocumento;
}
