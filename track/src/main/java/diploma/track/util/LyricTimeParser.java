package diploma.track.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LyricTimeParser {

    private static final Pattern PATTERN = Pattern.compile("(\\d{2}):(\\d{2})\\.(\\d{2})");

    public LyricTiming parseLyricTiming(String timing) throws ParseException {
        Matcher matcher = PATTERN.matcher(timing);
        if (matcher.find()) {
            return LyricTiming.builder()
                    .minutes(Integer.parseInt(matcher.group(1)))
                    .seconds(Integer.parseInt(matcher.group(2)))
                    .millis(Integer.parseInt(matcher.group(3)))
                    .build();
        } else throw new RuntimeException();
    }
}
