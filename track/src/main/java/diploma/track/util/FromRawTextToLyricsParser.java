package diploma.track.util;

import diploma.track.dto.SyncLyric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FromRawTextToLyricsParser {

    private final LyricTimeParser lyricTimeParser;
    private final int START_BRACKET_POSITION = 1;
    private final int CLOSE_BRACKET_POSITION = 9;

    public List<SyncLyric> toListLyricsFromRawText(String rawText) throws ParseException {
        String[] splitted = rawText.split("\n");
        List<SyncLyric> result = new ArrayList<>();
        for (int i = 0; i < splitted.length - 1; i++) {
            result.add(SyncLyric.builder()
                    .number(i + 1)
                    .originalText(splitted[i].substring(CLOSE_BRACKET_POSITION + 2))
                    .beginTime(lyricTimeParser.parseLyricTiming(splitted[i].substring(START_BRACKET_POSITION, CLOSE_BRACKET_POSITION)))
                    .endTime(lyricTimeParser.parseLyricTiming(splitted[i + 1].substring(START_BRACKET_POSITION, CLOSE_BRACKET_POSITION)))
                    .build());
        }
        return result;
    }
}
