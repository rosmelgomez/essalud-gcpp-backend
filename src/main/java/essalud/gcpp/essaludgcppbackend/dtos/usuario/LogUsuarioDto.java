package essalud.gcpp.essaludgcppbackend.dtos.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogUsuarioDto {
    String action;
    String status;
    String message;
    String url;
}
