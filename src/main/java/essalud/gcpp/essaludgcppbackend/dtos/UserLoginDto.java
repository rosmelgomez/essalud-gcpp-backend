package essalud.gcpp.essaludgcppbackend.dtos;

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
