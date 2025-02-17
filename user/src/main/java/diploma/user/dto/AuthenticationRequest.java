package diploma.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    @NotBlank(message = "login cannot be blank!")
    private String username;

    @NotBlank(message = "password cannot be blank!")
    private String password;
}
