package essalud.gcpp.essaludgcppbackend.model.usuario;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    /// relacion con usuario_rol
    @OneToMany(mappedBy = "rol_user" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRol> roles_users;

}
