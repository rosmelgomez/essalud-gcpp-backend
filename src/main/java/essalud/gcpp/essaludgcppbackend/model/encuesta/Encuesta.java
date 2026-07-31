package essalud.gcpp.essaludgcppbackend.model.encuesta;

import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "encuesta")
public class Encuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codcas", nullable = false)
    private String codcas;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    /// relation user.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Usuario user_encuesta;

    /// relation modulo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo")
    private Modulo modulo_encuesta;

    /// relation categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria_encuesta;

    /// relation subcategoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subcategoria")
    private SubCategoria subcategoria_encuesta;

    /// relation variable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variable")
    private Variable variable_encuesta;

}
