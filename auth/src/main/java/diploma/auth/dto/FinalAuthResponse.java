package diploma.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinalAuthResponse {

    boolean isSuccessful;
    Long userId;
    String token;
}
