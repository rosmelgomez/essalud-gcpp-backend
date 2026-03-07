package essalud.gcpp.essaludgcppbackend.model;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "cenasicod")
    private String cenasicod;

    @Column(name = "redasiscod")
    private String redasiscod;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_creation", nullable = false)
    private Date date_creation;

    @Column(name = "date_update", nullable = false)
    private Date date_update;

    @Column(name = "flag", nullable = false)
    private Boolean flag;

    @Column(name = "rol", nullable = false)
    private String rol;

    // One user can have many log entries.
    @OneToMany(mappedBy = "user_logs", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Logs> logs_user;
}