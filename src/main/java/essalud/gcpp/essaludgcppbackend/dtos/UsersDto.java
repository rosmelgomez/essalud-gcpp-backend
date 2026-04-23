package essalud.gcpp.essaludgcppbackend.dtos;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDto {
    private Integer id;

    private String name;

    private String lastname;

    private String dni;

    private String codcas;

    private String code_red;

    private String email;

    private String username;

    private String password;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private Boolean flag;

    private List<String> roles;
}
