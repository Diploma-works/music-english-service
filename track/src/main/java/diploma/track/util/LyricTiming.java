package diploma.track.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LyricTiming {

    int minutes;
    int seconds;
    int millis;
}
