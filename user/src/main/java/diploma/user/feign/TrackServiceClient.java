package diploma.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("track")
public interface TrackServiceClient {

    @GetMapping("/progress//users/{username}")
    ResponseEntity<Double> countUserProgress(@PathVariable String username);
}