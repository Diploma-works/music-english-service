package diploma.playlist.mapper;

import diploma.playlist.dto.PlaylistInfoWithoutTracks;
import diploma.playlist.entity.Playlist;
import diploma.playlist.feign.TrackServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistWithoutTracksMapper implements Mapper<PlaylistInfoWithoutTracks, Playlist>{

    private final TrackServiceClient trackServiceClient;

    @Override
    public PlaylistInfoWithoutTracks mapToDto(Playlist entity) {
        return PlaylistInfoWithoutTracks.builder()
                .yandexPlaylistId(entity.getYandexPlaylistId())
                .title(entity.getTitle())
                .build();
    }
}
