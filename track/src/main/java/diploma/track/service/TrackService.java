package diploma.track.service;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.SyncLyric;
import diploma.track.dto.TrackInfo;
import diploma.track.dto.TrackReadDto;
import diploma.track.dto.TrackReadDtoWithParsedLyrics;
import diploma.track.entity.Track;
import diploma.track.entity.TrackTask;
import diploma.track.mapper.TrackReadMapper;
import diploma.track.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final TrackReadMapper trackReadMapper;
    private final TrackTaskService trackTaskService;
    private final MusicDownloaderService musicDownloaderService;
    private final ProgressService progressService;
    private final AuthenticationFacade authenticationFacade;

    public Optional<Track> findTrackById(String trackId) {
        return trackRepository.findById(trackId);
    }

    public TrackReadDtoWithParsedLyrics prepareTaskByTrackId(String trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(EntityNotFoundException::new);
        List<Integer> lyricNumbersWithCompletedTasks = trackTaskService.findAllByTrackId(trackId, authenticationFacade.getAuthentication().getName()).stream().map(TrackTask::getLyricNumber).toList();
        TrackReadDtoWithParsedLyrics trackWithLyricsById = musicDownloaderService.getTrackWithLyricsById(trackId);
        List<SyncLyric> filteredSyncLyricsList = trackWithLyricsById.getSync_lyrics().stream().filter(syncLyric -> !lyricNumbersWithCompletedTasks.contains(syncLyric.getNumber())).toList();
        return TrackReadDtoWithParsedLyrics.builder()
                .id(trackId)
                .title(track.getTitle())
                .authors(trackWithLyricsById.getAuthors())
                .sync_lyrics(filteredSyncLyricsList)
                .content_warning(trackWithLyricsById.getContent_warning())
                .build();
    }

    public TrackInfo getTrackInfo(String trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(EntityNotFoundException::new);
        return TrackInfo.builder()
                .id(track.getId())
                .title(track.getTitle())
                .authors(Arrays.asList(trackId.split(",\\s*")))
                .progress(progressService.countTrackProgress(track, authenticationFacade.getAuthentication().getName()))
                .build();
    }

    public boolean saveAll(List<TrackReadDto> trackReadDtos) {
        trackRepository.saveAll(trackReadDtos.stream()
                .map(trackReadMapper::mapToEntity).toList());
        return true;
    }
}
