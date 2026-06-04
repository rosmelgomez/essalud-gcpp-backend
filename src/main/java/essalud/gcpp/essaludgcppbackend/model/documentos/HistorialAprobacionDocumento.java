package essalud.gcpp.essaludgcppbackend.model.documentos;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historial_aprobacion_documento")
public class HistorialAprobacionDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estado_anterior")
    private String estado_anterior;

    @Column(name = "estado_nuevo", nullable = false)
    private String estado_nuevo;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fecha_accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_accion")
    private Users user_accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flujo_aprobacion", nullable = false)
    private FlujoAprobacionDocumento flujo_aprobacion;
}
