package diploma.user.dto;

import diploma.user.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReadDto {

    private Long id;
    private String username;
    private Role role;
}
