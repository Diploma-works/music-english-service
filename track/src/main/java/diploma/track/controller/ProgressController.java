package diploma.track.controller;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.PlaylistProgressResponse;
import diploma.track.dto.PlaylistWithTrackIDs;
import diploma.track.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping("/track/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> countTrackProgress(@PathVariable String id) {
        return ResponseEntity.ok(progressService.countTrackProgress(id, authenticationFacade.getAuthentication().getName()));
    }

    @PostMapping("/playlist")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PlaylistProgressResponse> countPlaylistProgress(@RequestBody PlaylistWithTrackIDs request) {
        return ResponseEntity.ok(progressService.getPlaylistProgress(request, request.getUsername()));
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Double> countUserProgress(@PathVariable String username) {
        return ResponseEntity.ok(progressService.countUserProgress(username));
    }
}
