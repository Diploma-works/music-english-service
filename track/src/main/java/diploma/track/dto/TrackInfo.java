package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackInfo {

    String id;
    double progress;
    String title;
    List<String> authors;
}
