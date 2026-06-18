package essalud.gcpp.essaludgcppbackend.model.homologacion;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clasificacion_camas")
public class ClasificacionCamas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codcas", nullable = false)
    private String codcas;

        @Column(name = "camcod", nullable = false)
    private String camcod;

    @Column(name = "arehoscod", nullable = false)
    private String arehoscod;

    @Column(name = "habcod", nullable = false)
    private String habcod;

    @Column(name = "servhoscod", nullable = false)
    private String servhoscod;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

}
