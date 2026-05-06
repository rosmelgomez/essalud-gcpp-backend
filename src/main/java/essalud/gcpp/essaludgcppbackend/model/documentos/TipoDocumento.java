package essalud.gcpp.essaludgcppbackend.model.documentos;

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
@Table(name = "tipo_documento")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "extensiones_permitidas", nullable = false)
    private String extensiones_permitidas;

    @Column(name = "requiere_aprobacion", nullable = false)
    private Boolean requiere_aprobacion;

    @Column(name = "max_size_mb", nullable = false)
    private Integer max_size_mb;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    /// relacion con documento
    @OneToMany(mappedBy = "tipo_documento" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Documento> documentos;

}
