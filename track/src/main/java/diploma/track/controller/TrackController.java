package diploma.track.controller;

import diploma.track.dto.SyncLyric;
import diploma.track.dto.TrackInfo;
import diploma.track.dto.TrackReadDto;
import diploma.track.dto.TrackReadDtoWithParsedLyrics;
import diploma.track.service.LyricTrimService;
import diploma.track.service.MusicDownloaderService;
import diploma.track.service.TrackService;
import diploma.track.util.LyricTiming;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static diploma.track.util.AudioFormatConstants.MP3_FORMAT;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final MusicDownloaderService musicDownloaderService;
    private final LyricTrimService lyricTrimService;
    private final TrackService trackService;

    @GetMapping("/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TrackReadDtoWithParsedLyrics> getTrackReadDtoById(@PathVariable String trackId) {
        return ResponseEntity.ok(musicDownloaderService.getTrackWithLyricsById(trackId));
    }

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Boolean> saveAllTracks(@RequestBody List<TrackReadDto> tracksToSave) {
        return new ResponseEntity<>(trackService.saveAll(tracksToSave), HttpStatus.OK);
    }

    @GetMapping("/{id}/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TrackInfo> getTrackInfo(@PathVariable String id) {
        return ResponseEntity.ok(trackService.getTrackInfo(id));
    }
}
