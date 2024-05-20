package diploma.playlist.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadTrackResponse {

    private boolean successful;
    private String reason;
}
