package essalud.gcpp.essaludgcppbackend.model.usuario;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "usuario_rol")
public class UsuarioRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    /// relacion con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Usuario usuario_rol;


    /// relacion con Rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    private Rol rol_user;

}
