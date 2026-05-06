package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoDto {

    private Integer id;

    private String titulo;

    private String descripcion;

    private String extencion;

    private String usuario_creation;

    private String usuario_update;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private MultipartFile archivo;
}
