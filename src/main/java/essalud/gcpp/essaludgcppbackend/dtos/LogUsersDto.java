package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogUsersDto {
    String action;
    String status;
    String message;
    String url;
}
