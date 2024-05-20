package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadTrackResponse {

    boolean successful;
    String reason;
}
