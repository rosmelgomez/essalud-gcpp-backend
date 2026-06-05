package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResolucionAprobacionDocumentoDto {
    private Integer id;
    private Integer id_solicitud_aprobacion;
    private Integer id_usuario_revisor;
    private String status;
    private String comentarioAprobador;
    private LocalDateTime fechaResolucion;
}
