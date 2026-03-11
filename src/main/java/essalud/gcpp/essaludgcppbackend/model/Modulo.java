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
@Table(name = "modulo")
public class Modulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_modulo", nullable = false)
    private String name_modulo;

    @Column(name = "active", nullable = false)
    private Boolean active;

    /// Relation categoria
    @OneToMany(mappedBy = "modulo_categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Categoria> categorias;

    /// relation encuesta
    @OneToMany(mappedBy = "modulo_encuestas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;

}
