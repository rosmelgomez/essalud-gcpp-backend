package essalud.gcpp.essaludgcppbackend.model.encuesta;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_categoria", nullable = false)
    private String name_categoria;

    @Column(name = "active", nullable = false)
    private Boolean active;

    /// relation modulo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo")
    private Modulo modulo_categoria;

    /// Relation subcategoria
    @OneToMany(mappedBy = "categoria_subcategoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubCategoria> subcategorias;

    /// relation encuesta
    @OneToMany(mappedBy = "categoria_encuestas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;

}
