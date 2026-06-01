package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VersionDocumentoDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String extencion;
    private Integer version;
    private Double size;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private MultipartFile archivo;
    private Integer id_user_creation;
    private Integer id_documento;
    ///flujo de aprobacion
   private Integer id_user_aprobador;
}
