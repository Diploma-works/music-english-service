package diploma.user.service;

import diploma.user.autnentication.AuthenticationFacade;
import diploma.user.dto.*;
import diploma.user.entity.User;
import diploma.user.feign.PlaylistServiceClient;
import diploma.user.feign.TrackServiceClient;
import diploma.user.mapper.UserCreateMapper;
import diploma.user.mapper.UserReadMapper;
import diploma.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;
    private final PasswordEncoder passwordEncoder;
    private final TrackServiceClient trackServiceClient;
    private final AuthenticationFacade authenticationFacade;
    private final PlaylistServiceClient playlistServiceClient;

    @Transactional
    public Optional<UserReadDto> createUser(UserCreateDto userCreateDto) {
        return Optional.of(userRepository.save(userCreateMapper.mapToEntity(userCreateDto)))
                .map(userReadMapper::mapToDto);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return userRepository.findUserByUsername(authenticationRequest.getUsername())
                .map(user -> {
                    UserReadDto userReadDto = userReadMapper.mapToDto(user);
                    return passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()) ? AuthenticationResponse.builder().userReadDto(userReadDto).isSuccessful(true).build()
                            : AuthenticationResponse.builder().isSuccessful(false).build();
                }).orElse(AuthenticationResponse.builder().isSuccessful(false).build());
    }

    public UserInfoDto getUserInfoDto(String username) {
        String loggedUsername = authenticationFacade.getAuthentication().getName();
        if (!username.equals(loggedUsername)) throw new RuntimeException();
        return userRepository.findUserByUsername(username)
                .map(user -> UserInfoDto.builder()
                        .username(username)
                        .age(user.getAge())
                        .email(user.getEmail())
                        .playlists(playlistServiceClient.getUserPlaylistsInfo(username).getBody())
                        .userSummaryProgress(trackServiceClient.countUserProgress(username).getBody())
                        .build()
                ).orElseThrow(EntityNotFoundException::new);
    }
}
