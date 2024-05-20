package diploma.playlist.service;

import diploma.playlist.autnentication.AuthenticationFacade;
import diploma.playlist.dto.PlaylistInfoWithoutTracks;
import diploma.playlist.dto.PlaylistReadDto;
import diploma.playlist.dto.PlaylistWithTrackIDs;
import diploma.playlist.dto.TrackReadDto;
import diploma.playlist.entity.Playlist;
import diploma.playlist.exception.IncorrectPlaylistUrlException;
import diploma.playlist.repository.PlaylistRepository;
import diploma.playlist.util.ExceptionConstants;
import diploma.playlist.util.PlaylistIdHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistIdHandler playlistIdHandler;
    private final AuthenticationFacade authenticationFacade;
    private final MusicDownloaderService musicDownloaderService;

    public PlaylistService(PlaylistRepository playlistRepository, PlaylistIdHandler playlistIdHandler, AuthenticationFacade authenticationFacade, @Lazy MusicDownloaderService musicDownloaderService) {
        this.playlistRepository = playlistRepository;
        this.playlistIdHandler = playlistIdHandler;
        this.authenticationFacade = authenticationFacade;
        this.musicDownloaderService = musicDownloaderService;
    }

    public Playlist createPlaylistFromPlaylistReadDto(PlaylistReadDto playlistReadDto) {
        String username = authenticationFacade.getAuthentication().getName();
        Playlist createdPlaylistEntity = playlistRepository.save(
                Playlist.builder()
                        .yandexPlaylistId(playlistReadDto.getId())
                        .title(playlistReadDto.getTitle())
                        .username(username)
                        .trackCount(playlistReadDto.getTracks().size())
                        .build()
        );
        return createdPlaylistEntity;
    }

    public Optional<Playlist> findByYandexPlaylistIdAndUsername(String playlistUrl, String username) {
        String playlistId = playlistIdHandler.extractPlaylistId(playlistUrl);
        if (playlistId == null) throw new IncorrectPlaylistUrlException(ExceptionConstants.INCORRECT_PLAYLIST_TEXT);
        return playlistRepository.findPlaylistByYandexPlaylistIdAndUsername(playlistId, username);
    }

    public List<PlaylistWithTrackIDs> getAllPlaylistsToPrepareProgressByUsername(String username) {
        List<Playlist> allByUsername = playlistRepository.findAllByUsername(username);
        return allByUsername.stream()
                .map(playlist -> musicDownloaderService.getPlaylistInfoById(playlist.getYandexPlaylistId(), username))
                .map(playlistReadDto -> PlaylistWithTrackIDs.builder()
                        .yandexPlaylistId(playlistReadDto.getId())
                        .trackIds(playlistReadDto.getTracks().stream().map(TrackReadDto::getId).collect(Collectors.toList())).build())
                .collect(Collectors.toList());
    }
}
