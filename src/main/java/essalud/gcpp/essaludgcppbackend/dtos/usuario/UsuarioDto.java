package essalud.gcpp.essaludgcppbackend.dtos.usuario;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDto {
    private Integer id;

    private String name;

    private String lastname;

    private String dni;

    private String codcas;

    private String codred;

    private String email;

    private String username;

    private String password;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private Boolean flag;

    private Boolean firstLogin;

    private List<String> roles;
}



