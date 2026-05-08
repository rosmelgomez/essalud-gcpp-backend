package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.descriptor.java.BlobJavaType;


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

    @Column(name = "comentario")
    private String comentario;


    @Column(name = "extencion", nullable = false)
    private String extencion;

    @Column(name = "version" , nullable = false)
    private String version;

    @Column(name = "size" , nullable = false)
    private double size;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Lob
    @Column(name = "archivo", nullable = false)
    private byte[] archivo;

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
