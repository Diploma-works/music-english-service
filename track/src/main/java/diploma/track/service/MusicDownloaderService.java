package diploma.track.service;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.DownloadTrackRequest;
import diploma.track.dto.DownloadTrackResponse;
import diploma.track.dto.TrackReadDtoWithParsedLyrics;
import diploma.track.dto.TrackReadDtoWithRawLyrics;
import diploma.track.util.FromRawTextToLyricsParser;
import diploma.track.util.MusicServiceUrl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class MusicDownloaderService {

    @Value("${music.service.url}")
    private String musicServiceUrl;

    private final RestTemplate restTemplate;
    private final AuthenticationFacade authenticationFacade;
    private final FromRawTextToLyricsParser fromRawTextToLyricsParser;

    @SneakyThrows
    public TrackReadDtoWithParsedLyrics getTrackWithLyricsById(String trackId) {
        TrackReadDtoWithRawLyrics response = restTemplate.getForObject(musicServiceUrl + MusicServiceUrl.DOWNLOAD_TRACK_URL + "/" + trackId, TrackReadDtoWithRawLyrics.class);
        if (response == null) throw new RuntimeException();
        return toRawLyricsDtoToParseLyricsDto(response);
    }

    public DownloadTrackResponse uploadTrackToS3Storage(String trackId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        DownloadTrackResponse downloadTrackResponse = restTemplate.postForObject(musicServiceUrl + MusicServiceUrl.DOWNLOAD_TRACK_URL,
                DownloadTrackRequest.builder().track_id(trackId).username(authenticationFacade.getAuthentication().getName()).build(),
                DownloadTrackResponse.class);
        return downloadTrackResponse;
    }

    private TrackReadDtoWithParsedLyrics toRawLyricsDtoToParseLyricsDto(TrackReadDtoWithRawLyrics trackReadDtoWithRawLyrics) throws ParseException {
        return TrackReadDtoWithParsedLyrics.builder()
                .id(trackReadDtoWithRawLyrics.getId())
                .title(trackReadDtoWithRawLyrics.getTitle())
                .authors(trackReadDtoWithRawLyrics.getAuthors())
                .sync_lyrics(fromRawTextToLyricsParser.toListLyricsFromRawText(trackReadDtoWithRawLyrics.getSync_lyrics()))
                .build();
    }
}
