package diploma.user.mapper;

import diploma.user.dto.UserReadDto;
import diploma.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<UserReadDto, User> {

    @Override
    public UserReadDto mapToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .build();
    }
}
