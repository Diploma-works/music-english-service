package diploma.playlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistHandleRequest {

    @NotBlank
    String username;

    @NotBlank
    String playlist_url;
}
