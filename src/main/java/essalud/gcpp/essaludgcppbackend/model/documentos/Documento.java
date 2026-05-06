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
@Table(name = "documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "extencion", nullable = false)
    private String extencion;

    @Column(name = "usuario_creation", nullable = false)
    private String usuario_creation;

    @Column(name = "usuario_update", nullable = false)
    private String usuario_update;

    @Column(name = "codcas", nullable = false)
    private String codcas;

    @Column(name = "cod_red", nullable = false)
    private String cod_red;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "archivo", nullable = false)
    private byte[] archivo;

}
