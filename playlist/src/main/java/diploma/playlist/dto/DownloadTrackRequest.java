package diploma.playlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadTrackRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String track_id;
}
