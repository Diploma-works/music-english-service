package diploma.playlist.controller;

import diploma.playlist.autnentication.AuthenticationFacade;
import diploma.playlist.dto.*;
import diploma.playlist.service.MusicDownloaderService;
import diploma.playlist.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final MusicDownloaderService musicDownloaderService;
    private final PlaylistService playlistService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/tracks/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DownloadTrackResponse> downloadTrack(@RequestBody @Valid DownloadTrackRequest downloadTrackRequest) {
        return ResponseEntity.ok(musicDownloaderService.downloadTrackById(downloadTrackRequest));
    }

    @PostMapping("/playlists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistReadDto> handlePlaylist(@RequestBody @Valid PlaylistHandleRequest playlistHandleRequest) {
        return ResponseEntity.ok(musicDownloaderService.handlePlaylist(playlistHandleRequest));
    }

    @GetMapping("/playlists/{playlistId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistReadDto> getPlaylistInfoById(@PathVariable String playlistId) {
        return ResponseEntity.ok(musicDownloaderService.getPlaylistInfoById(playlistId, authenticationFacade.getAuthentication().getName()));
    }

    @GetMapping("/playlists/all/{username}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PlaylistInfoWithoutTracks>> getUserPlaylistsInfo(@PathVariable String username) {
        return ResponseEntity.ok(musicDownloaderService.getAllUserPlaylistsInfo(username));
    }

    @GetMapping("/playlists/{username}/progress")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PlaylistWithTrackIDs>> getAllPlaylistsToPrepareProgressByUsername(@PathVariable String username) {
        return ResponseEntity.ok(playlistService.getAllPlaylistsToPrepareProgressByUsername(username));
    }
}
