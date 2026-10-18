package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CatalogoExtensionDto {
    private Integer id;
    private String nombre;
    private String mimeType;
}
