package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadTrackRequest {

    String username;
    String track_id;
}
