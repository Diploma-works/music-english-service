package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackReadDto {

    String id;
    List<String> authors;
    String title;
    String content_warning;
    Integer lyrics_count;
}
