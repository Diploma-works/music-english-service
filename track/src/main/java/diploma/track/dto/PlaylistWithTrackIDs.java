package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaylistWithTrackIDs {

    String username;
    String yandexPlaylistId;
    List<String> trackIds;
}
