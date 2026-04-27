package essalud.gcpp.essaludgcppbackend.dtos;


import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoDto {
    private Integer id;

    private String nombre;

    private String descripcion;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private String usuario_creation;

    private String usuario_update;

    private byte[] archivo;
}
