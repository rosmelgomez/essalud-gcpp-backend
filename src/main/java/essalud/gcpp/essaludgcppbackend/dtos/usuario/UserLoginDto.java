package essalud.gcpp.essaludgcppbackend.dtos.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDto {

    private String username;

    private String password;
}
