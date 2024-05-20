package diploma.track.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
@Builder
public class TaskReadDto {

    private File audio;
    private String trackId;
    private String title;
    private List<String> authors;
    private String text;
    private Integer lyricNumber;
    @JsonProperty("shuffled_text")
    private List<String> shuffledText;
}
