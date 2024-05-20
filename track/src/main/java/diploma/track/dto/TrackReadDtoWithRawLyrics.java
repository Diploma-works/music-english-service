package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackReadDtoWithRawLyrics {

    String id;
    List<String> authors;
    String sync_lyrics;
    String title;
    String content_warning;
}
