package diploma.user.feign;

import diploma.user.dto.PlaylistInfoWithoutTracks;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("playlist")
public interface PlaylistServiceClient {

    @GetMapping("/playlist/playlists/all/{username}")
    ResponseEntity<List<PlaylistInfoWithoutTracks>> getUserPlaylistsInfo(@PathVariable String username);
}