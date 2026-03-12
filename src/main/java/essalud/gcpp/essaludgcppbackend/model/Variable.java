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
@Table(name = "variable")
public class Variable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_variable", nullable = false)
    private String name_variable;

    @Column(name = "active", nullable = false)
    private Boolean active;

    /// relation categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subcategoria")
    private SubCategoria subcategoria_variable;

    /// relation encuesta
    @OneToMany(mappedBy = "variable_encuestas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Encuesta> encuestas;
}
