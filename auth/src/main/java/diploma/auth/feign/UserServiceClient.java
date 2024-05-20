package diploma.auth.feign;

import diploma.auth.dto.AuthenticationRequest;
import diploma.auth.dto.AuthenticationResponse;
import diploma.auth.dto.UserCreateDto;
import diploma.auth.dto.UserReadDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserServiceClient {

    @PostMapping("/users/register")
    UserReadDto register(@RequestBody @Valid UserCreateDto userCreateDto);

    @PostMapping("/users/authenticate")
    AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest);
}
