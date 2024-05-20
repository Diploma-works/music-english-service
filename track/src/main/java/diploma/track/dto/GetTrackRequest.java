package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTrackRequest {

    String track_id;
}
