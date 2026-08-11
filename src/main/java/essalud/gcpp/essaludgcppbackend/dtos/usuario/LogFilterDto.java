package essalud.gcpp.essaludgcppbackend.dtos.usuario;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogFilterDto {
    private String dni;
    private String full_name;
    private String ip_user;
    private String url;
    private String browser;
    private String user_role;
    private String codcas;
    private String code_red;
    private LocalDateTime date;
    private Integer id_user;
}
