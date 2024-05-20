package diploma.auth.controller;

import diploma.auth.dto.FinalAuthResponse;
import diploma.auth.dto.AuthenticationRequest;
import diploma.auth.dto.UserCreateDto;
import diploma.auth.dto.ValidateTokenResponse;
import diploma.auth.service.AuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<FinalAuthResponse> register(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(authService.registerUser(userCreateDto));
    }

    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<FinalAuthResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        FinalAuthResponse finalAuthResponse = authService.authenticate(authenticationRequest);
        return finalAuthResponse.isSuccessful() ? ResponseEntity.ok(finalAuthResponse)
                : new ResponseEntity<>(finalAuthResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/validate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ValidateTokenResponse> validateToken(HttpServletRequest httpServletRequest) {
        ValidateTokenResponse validateTokenResponse = authService.validateToken(httpServletRequest);
        return validateTokenResponse.isAuthenticated() ? ResponseEntity.ok(validateTokenResponse)
                : new ResponseEntity(validateTokenResponse, HttpStatus.NOT_FOUND);
    }
}
