package diploma.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private boolean isSuccessful;
    private UserReadDto userReadDto;
}
