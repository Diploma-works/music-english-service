package diploma.user.controller;

import diploma.user.dto.*;
import diploma.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public UserReadDto register(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return userService.authenticate(authenticationRequest);
    }

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserInfoDto(username));
    }
}
