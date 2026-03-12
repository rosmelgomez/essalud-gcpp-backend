package essalud.gcpp.essaludgcppbackend.model;

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
    private Users user_encuestas;

    /// relation modulo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo")
    private Modulo modulo_encuestas;

    /// relation categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria_encuestas;

    /// relation subcategoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subcategoria")
    private SubCategoria subcategoria_encuestas;

    /// relation variable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variable")
    private Variable variable_encuestas;

}
