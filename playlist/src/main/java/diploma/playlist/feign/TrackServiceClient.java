package diploma.playlist.feign;

import diploma.playlist.dto.PlaylistProgressResponse;
import diploma.playlist.dto.PlaylistWithTrackIDs;
import diploma.playlist.dto.TrackReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("track")
public interface TrackServiceClient {

    @PostMapping("/tracks/save")
    ResponseEntity<Boolean> saveAllTracks(@RequestBody List<TrackReadDto> tracksToSave);

    @PostMapping("/progress/playlist")
    ResponseEntity<PlaylistProgressResponse> countPlaylistProgress(@RequestBody PlaylistWithTrackIDs request);
}
