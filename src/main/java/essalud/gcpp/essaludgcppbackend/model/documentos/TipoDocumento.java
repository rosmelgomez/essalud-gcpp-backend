package essalud.gcpp.essaludgcppbackend.model.documentos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "max_tamano_mb", nullable = false)
    private Integer max_tamano_mb;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

}
