package diploma.playlist.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PlaylistProgressResponse {

    String yandexPlaylistId;
    double progress;
}
