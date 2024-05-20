package diploma.track.dto;

import diploma.track.util.LyricTiming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SyncLyric {

    private int number;
    private String originalText;
    private LyricTiming beginTime;
    private LyricTiming endTime;
}
