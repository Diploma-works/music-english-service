package diploma.track.service;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.SyncLyric;
import diploma.track.dto.TaskAnswerDto;
import diploma.track.dto.TaskReadDto;
import diploma.track.dto.TrackReadDtoWithParsedLyrics;
import diploma.track.entity.TrackTask;
import diploma.track.repository.TrackTaskRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class TrackTaskService {

    private final TrackTaskRepository trackTaskRepository;
    private final AuthenticationFacade authenticationFacade;
    private final TrackService trackService;
    private final LyricTrimService lyricTrimService;

    public TrackTaskService(TrackTaskRepository trackTaskRepository, AuthenticationFacade authenticationFacade, @Lazy TrackService trackService, LyricTrimService lyricTrimService) {
        this.trackTaskRepository = trackTaskRepository;
        this.authenticationFacade = authenticationFacade;
        this.trackService = trackService;
        this.lyricTrimService = lyricTrimService;
    }

    public TaskReadDto createTask(String trackId) {
        TrackReadDtoWithParsedLyrics trackReadDtoWithParsedLyrics = trackService.prepareTaskByTrackId(trackId);
        List<SyncLyric> syncLyrics = trackReadDtoWithParsedLyrics.getSync_lyrics();
        SyncLyric syncLyricForTask = getRandomLyricFromList(syncLyrics);
        File audioData = lyricTrimService.trim(trackId, syncLyricForTask);
        List<String> splittedText = Arrays.asList(syncLyricForTask.getOriginalText().split(" \\s*"));
        Collections.shuffle(splittedText);
        return TaskReadDto.builder()
                .audio(audioData)
                .trackId(trackId)
                .title(trackReadDtoWithParsedLyrics.getTitle())
                .authors(trackReadDtoWithParsedLyrics.getAuthors())
                .text(syncLyricForTask.getOriginalText())
                .shuffledText(splittedText)
                .lyricNumber(syncLyricForTask.getNumber())
                .build();
    }

    public boolean handleTask(TaskAnswerDto taskAnswerDto) {
        if (!taskAnswerDto.getAnswerText().equals(taskAnswerDto.getOriginalText())) return false;
        trackTaskRepository.save(TrackTask.builder()
                .track(trackService.findTrackById(taskAnswerDto.getTrackId()).get())
                .lyricNumber(taskAnswerDto.getLyricNumber())
                .username(authenticationFacade.getAuthentication().getName())
                .playlistYandexId(taskAnswerDto.getYandexPlaylistId())
                .build());
        return true;
    }

    public List<TrackTask> findAllByTrackId(String trackId, String username) {
        return trackTaskRepository.findAllByTrackIdAndUsername(trackId, username);
    }

    private SyncLyric getRandomLyricFromList(List<SyncLyric> syncLyrics) {
        Random random = new Random();
        int lyricNumber = random.nextInt(syncLyrics.size());
        return syncLyrics.get(lyricNumber);
    }
}
