package diploma.playlist.service;

import diploma.playlist.autnentication.AuthenticationFacade;
import diploma.playlist.dto.*;
import diploma.playlist.exception.PlaylistAlreadyExistException;
import diploma.playlist.repository.PlaylistRepository;
import diploma.playlist.util.ExceptionConstants;
import diploma.playlist.feign.TrackServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static diploma.playlist.util.MusicServiceUrl.*;

@Service
@RequiredArgsConstructor
public class MusicDownloaderService {

    @Value("${music.service.url}")
    private String musicServiceUrl;

    private final RestTemplate restTemplate;
    private final PlaylistService playlistService;
    private final AuthenticationFacade authenticationFacade;
    private final TrackServiceClient trackServiceClient;
    private final PlaylistRepository playlistRepository;

    public PlaylistReadDto handlePlaylist(PlaylistHandleRequest playlistHandleRequest) {
        playlistService.findByYandexPlaylistIdAndUsername(playlistHandleRequest.getPlaylist_url(), authenticationFacade.getAuthentication().getName()).ifPresent(playlist -> {
            throw new PlaylistAlreadyExistException(ExceptionConstants.PLAYLIST_ALREADY_EXIST_TEXT);
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        PlaylistReadDto playlistReadDto = restTemplate.postForObject(musicServiceUrl + PLAYLISTS_URL, playlistHandleRequest, PlaylistReadDto.class);
        if (playlistReadDto != null) {
            playlistService.createPlaylistFromPlaylistReadDto(playlistReadDto);
            trackServiceClient.saveAllTracks(playlistReadDto.getTracks());
        } else throw new RuntimeException();
        playlistReadDto.setProgress(0.0);
        return playlistReadDto;
    }

    public PlaylistReadDto getPlaylistInfoById(String playlistId, String username) {
        PlaylistReadDto playlistReadDto = restTemplate.getForObject(musicServiceUrl + PLAYLISTS_URL + "/" + playlistId, PlaylistReadDto.class);
        if (playlistReadDto == null) throw new RuntimeException();
        PlaylistProgressResponse body = trackServiceClient.countPlaylistProgress(PlaylistWithTrackIDs.builder()
                .username(username)
                .yandexPlaylistId(playlistId)
                .trackIds(playlistReadDto.getTracks().stream().map(TrackReadDto::getId).collect(Collectors.toList()))
                .build()).getBody();
        playlistReadDto.setProgress(body.getProgress());
        return playlistReadDto;
    }

    public List<PlaylistInfoWithoutTracks> getAllUserPlaylistsInfo(String username) {
        return playlistRepository.findAllByUsername(username)
                .stream().map(playlist -> getPlaylistInfoById(playlist.getYandexPlaylistId(), username))
                .map(playlistReadDto -> PlaylistInfoWithoutTracks.builder()
                        .yandexPlaylistId(playlistReadDto.getId())
                        .title(playlistReadDto.getTitle())
                        .playlistProgress(playlistReadDto.getProgress())
                        .build()).collect(Collectors.toList());
    }

    public DownloadTrackResponse downloadTrackById(DownloadTrackRequest downloadTrackRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(musicServiceUrl + DOWNLOAD_TRACK_URL, downloadTrackRequest, DownloadTrackResponse.class);
    }
}
