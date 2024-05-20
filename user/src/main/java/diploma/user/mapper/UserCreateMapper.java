package diploma.user.mapper;

import diploma.user.dto.UserCreateDto;
import diploma.user.entity.Role;
import diploma.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapToEntity(UserCreateDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .age(dto.getAge())
                .role(Role.USER)
                .build();
    }
}
