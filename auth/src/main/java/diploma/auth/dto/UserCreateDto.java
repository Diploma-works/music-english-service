package diploma.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {

    String username;
    String password;
    String email;
    Integer age;
}
