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
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "codcas")
    private String codcas;

    @Column(name = "codred")
    private String codred;

    @Column(name = "publico", nullable = false)
    private Boolean publico;

    @Column(name = "solo_mi_red", nullable = false)
    private Boolean solo_mi_red;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "date_delete")
    private LocalDateTime date_delete;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    /// relacion con usuario creador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_creation")
    private Usuario user_documento;

    /// relacion con tipo documento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipo_documento;

    /// relacion con version documento
    @OneToMany(mappedBy = "documento_version_documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VersionDocumento> version_documentos;

}
