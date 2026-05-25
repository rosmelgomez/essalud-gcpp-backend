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
@Table(name = "flujo_aprobacion_documento")
public class FlujoAprobacionDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comentario", nullable = false)
    private String comentario;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;


    /// relacion con usuario aprobador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_aprobador")
    private Users user_flujo_aprobacion;


    /// relacion con version_documento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_version_documento")
    private VersionDocumento version_documento_aprobacion;

    @OneToMany(mappedBy = "flujo_aprobacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistorialAprobacionDocumento> historialAprobaciones;
}
