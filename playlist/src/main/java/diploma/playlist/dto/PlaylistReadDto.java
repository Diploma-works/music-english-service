package diploma.playlist.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaylistReadDto {

    String id;
    String title;
    List<TrackReadDto> tracks;
    Double progress;
}
