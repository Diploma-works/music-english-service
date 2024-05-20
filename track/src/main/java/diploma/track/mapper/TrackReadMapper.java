package diploma.track.mapper;

import diploma.track.dto.TrackReadDto;
import diploma.track.entity.Track;
import org.springframework.stereotype.Component;

@Component
public class TrackReadMapper implements Mapper<TrackReadDto, Track> {

    @Override
    public Track mapToEntity(TrackReadDto dto) {
        return Track.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .authors(String.join(",", dto.getAuthors()))
                .lyricCount(dto.getLyrics_count())
                .build();
    }
}
