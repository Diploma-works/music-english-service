package diploma.track.service;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.PlaylistProgressResponse;
import diploma.track.dto.PlaylistWithTrackIDs;
import diploma.track.entity.Track;
import diploma.track.entity.TrackTask;
import diploma.track.feign.PlaylistServiceClient;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final TrackService trackService;
    private final TrackTaskService trackTaskService;
    private final AuthenticationFacade authenticationFacade;
    private final PlaylistServiceClient playlistServiceClient;

    public ProgressService(@Lazy TrackService trackService, TrackTaskService trackTaskService, AuthenticationFacade authenticationFacade, PlaylistServiceClient playlistServiceClient) {
        this.trackService = trackService;
        this.trackTaskService = trackTaskService;
        this.authenticationFacade = authenticationFacade;
        this.playlistServiceClient = playlistServiceClient;
    }

    public double countTrackProgress(Track track, String username) {
        List<Integer> lyricNumbersWithCompletedTasks = trackTaskService.findAllByTrackId(track.getId(), username).stream().map(TrackTask::getLyricNumber).toList();
        return ((double) lyricNumbersWithCompletedTasks.size() / track.getLyricCount());
    }

    public double countTrackProgress(String trackId, String username) {
        Track track = trackService.findTrackById(trackId).orElseThrow(EntityNotFoundException::new);
        List<Integer> lyricNumbersWithCompletedTasks = trackTaskService.findAllByTrackId(trackId, username).stream().map(TrackTask::getLyricNumber).toList();
        return ((double) lyricNumbersWithCompletedTasks.size() / track.getLyricCount());
    }

    public PlaylistProgressResponse getPlaylistProgress(PlaylistWithTrackIDs playlistProgressRequest, String username) {
        return PlaylistProgressResponse.builder()
                .progress(countPlaylistProgress(playlistProgressRequest.getTrackIds(), username))
                .yandexPlaylistId(playlistProgressRequest.getYandexPlaylistId())
                .build();
    }

    public double countPlaylistProgress(List<String> trackIds, String username) {
        int trackCount = trackIds.size();
        double sum = 0.0;
        for (String trackId : trackIds) {
            sum += (1.0 / trackCount) * countTrackProgress(trackId, username);
        }
        return sum;
    }

    public double countUserProgress(String username) {
        List<PlaylistWithTrackIDs> responses = playlistServiceClient.getAllPlaylistsToPrepareProgressByUsername(username).getBody();
        if (responses == null) return 0.0;
        double sum = 0.0;
        for (String playlistId : responses.stream().map(PlaylistWithTrackIDs::getYandexPlaylistId).toList()) {
            sum += countPlaylistProgress(responses.stream().filter(
                            playlistWithTrackIDs -> playlistWithTrackIDs.getYandexPlaylistId().equals(playlistId))
                    .map(PlaylistWithTrackIDs::getTrackIds)
                    .flatMap(List::stream)
                    .collect(Collectors.toList()), username);
        }
        return sum;
    }
}
