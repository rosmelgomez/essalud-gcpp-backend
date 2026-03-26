package essalud.gcpp.essaludgcppbackend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_user")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_user", nullable = false)
    private String ip_user;

    @Column(name = "http_method", nullable = false)
    private String http_method;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "browser", nullable = false)
    private String browser;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation;

    /// relation user.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Users user_log;


}
