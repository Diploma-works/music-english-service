package diploma.track.feign;

import diploma.track.dto.PlaylistWithTrackIDs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("playlist")
public interface PlaylistServiceClient {

    @GetMapping("/playlist/playlists/{username}/progress")
    ResponseEntity<List<PlaylistWithTrackIDs>> getAllPlaylistsToPrepareProgressByUsername(@PathVariable String username);
}
