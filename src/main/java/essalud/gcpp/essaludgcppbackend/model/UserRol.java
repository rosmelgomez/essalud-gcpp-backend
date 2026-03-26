package essalud.gcpp.essaludgcppbackend.model;

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
@Table(
        name = "user_rol",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "rol_id"})
        }
)
public class UserRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    /// relacion con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users usuario_rol;


    /// relacion con Rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Rol rol_user;



}
