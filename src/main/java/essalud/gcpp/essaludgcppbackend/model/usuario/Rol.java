package essalud.gcpp.essaludgcppbackend.model.usuario;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime date_update;

    @Column(name = "flag", nullable = false)
    private Boolean flag;


    /// relacion con usuario_rol
    @OneToMany(mappedBy = "rol_user" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UsuarioRol> roles_users;

}
