package diploma.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistInfoWithoutTracks {

    String yandexPlaylistId;
    String title;
    @JsonProperty("playlist_progress")
    double playlistProgress;
}
