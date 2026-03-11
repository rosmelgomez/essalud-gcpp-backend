package essalud.gcpp.essaludgcppbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subcategoria")
public class SubCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_subcategoria", nullable = false)
    private String name_categoria;

    @Column(name = "active", nullable = false)
    private Boolean active;

    /// relation categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria_subcategoria;

    /// Relation variable
    @OneToMany(mappedBy = "subcategoria_variable", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Variable> variables;

    /// relation encuesta
    @OneToMany(mappedBy = "subcategoria_encuestas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;
}
