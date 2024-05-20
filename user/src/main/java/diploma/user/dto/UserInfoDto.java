package diploma.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserInfoDto {

    String username;
    String email;
    Integer age;
    @JsonProperty("user_summary_progress")
    double userSummaryProgress;

    List<PlaylistInfoWithoutTracks> playlists;
}
