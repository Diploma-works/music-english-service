package diploma.auth.dto;

import diploma.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenResponse {

    Long userId;
    String username;
    List<Role> authorities;
    boolean isAuthenticated;
    String reason;
}
