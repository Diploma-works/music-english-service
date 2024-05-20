package diploma.auth.service;

import diploma.auth.dto.*;
import diploma.auth.entity.Role;
import diploma.auth.entity.User;
import diploma.auth.feign.UserServiceClient;
import diploma.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import static diploma.auth.util.JwtConstants.*;
import static diploma.auth.util.ValidateBadResponseReasons.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FinalAuthResponse registerUser(UserCreateDto userCreateDto) {
        UserReadDto userReadDto = userServiceClient.register(userCreateDto);
        return FinalAuthResponse.builder()
                .userId(userReadDto.getId())
                .token(jwtService.generateToken(new HashMap<>(), userReadDto))
                .build();
    }

    public FinalAuthResponse authenticate(AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = userServiceClient.authenticate(authenticationRequest);
        return userRepository.findUserByUsername(authenticationRequest.getUsername()).map(
                user -> {
                    return passwordEncoder.matches(authenticationRequest.getPassword(),
                            user.getPassword()) ?
                            FinalAuthResponse.builder()
                                    .isSuccessful(true)
                                    .userId(authenticationResponse.getUserReadDto().getId())
                                    .token(jwtService.generateToken(new HashMap<>(), authenticationResponse.getUserReadDto()))
                                    .build()
                            : FinalAuthResponse.builder().isSuccessful(false).build();
                }
        ).orElseThrow(EntityNotFoundException::new);
    }

    public ValidateTokenResponse validateToken(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return ValidateTokenResponse.builder().isAuthenticated(false).reason(BAD_AUTHORIZATION_HEADER).build();
        }
        String jwtToken = authHeader.substring(BEARER.length() + 1);
        String username = jwtService.extractUsername(jwtToken);
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) throw new EntityNotFoundException();
        if (jwtService.isTokenExpired(jwtToken))
            return ValidateTokenResponse.builder().isAuthenticated(false).reason(JWT_TOKEN_EXPIRED).build();
        return ValidateTokenResponse.builder()
                .isAuthenticated(true)
                .userId(jwtService.extractUserId(jwtToken))
                .username(jwtService.extractUsername(jwtToken))
                .authorities(jwtService.extractAuthorities(jwtToken).stream().map(Role::valueOf).toList())
                .build();
    }
}
