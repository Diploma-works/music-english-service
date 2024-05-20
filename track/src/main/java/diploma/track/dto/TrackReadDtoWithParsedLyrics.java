package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackReadDtoWithParsedLyrics {

    String id;
    List<String> authors;
    List<SyncLyric> sync_lyrics;
    String title;
    String content_warning;
}
