package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogFilter {
    private String dni;
    private String name;
    private String lastname;
    private String ip_user;
    private String url;
    private String browser;
    private String user_role;
    private String codcas;
    private String code_red;
    private LocalDateTime date;
}
