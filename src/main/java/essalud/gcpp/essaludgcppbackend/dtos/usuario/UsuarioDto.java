package essalud.gcpp.essaludgcppbackend.dtos.usuario;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

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

    private Boolean access_tmi;

    private Boolean first_Login;

    private List<String> roles;
}



