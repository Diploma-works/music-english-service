package diploma.track.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskAnswerDto {

    String trackId;
    String originalText;
    String answerText;
    String yandexPlaylistId;
    Integer lyricNumber;
}
