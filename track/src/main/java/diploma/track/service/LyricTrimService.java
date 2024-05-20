package diploma.track.service;

import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.dto.SyncLyric;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static diploma.track.util.AudioFormatConstants.*;
import static diploma.track.util.TrackDirectoryUtils.TRACKS_DIR;

@Service
@RequiredArgsConstructor
public class LyricTrimService {

    private final S3Service s3Service;
    private final AuthenticationFacade authenticationFacade;
    private final MusicDownloaderService musicDownloaderService;

    @SneakyThrows
    public File trim(String trackId, SyncLyric syncLyric) {
        String username = authenticationFacade.getAuthentication().getName();
        File userTracksDir = new File(TRACKS_DIR + File.separator + username);
        File audioFile = new File(userTracksDir + File.separator + trackId + MP3_FORMAT);
        if (!audioFile.exists()) {
            musicDownloaderService.uploadTrackToS3Storage(trackId);
            audioFile = s3Service.downloadTrackToFile(trackId + MP3_FORMAT);
        }
        File outputFile = new File(userTracksDir + File.separator + trackId + "-cutted" + "(" + syncLyric.getNumber() + ")" + MP3_FORMAT);
        File wavFile = convertToWav(audioFile);
        double startTimeMilliSeconds = (syncLyric.getBeginTime().getMinutes() * 60 * 1000) + (syncLyric.getBeginTime().getSeconds() * 1000) + syncLyric.getBeginTime().getMillis(); // Начало обрезки в секундах
        double endTimeMilliSeconds = (syncLyric.getEndTime().getMinutes() * 60 * 1000) + (syncLyric.getEndTime().getSeconds() * 1000) + syncLyric.getEndTime().getMillis();

        AudioInputStream inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(wavFile);
        } catch (UnsupportedAudioFileException | IOException ex) {
            throw new RuntimeException(ex);
        }
        AudioFormat format = inputStream.getFormat();

        long startFrame = (long) ((format.getFrameRate() / 1000.0) * startTimeMilliSeconds);
        long endFrame = (long) ((format.getFrameRate() / 1000.0) * endTimeMilliSeconds);

        long bytesToSkip = startFrame * format.getFrameSize();
        try {
            inputStream.skip(bytesToSkip);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        long framesToRead = endFrame - startFrame;
        AudioInputStream clippedStream = new AudioInputStream(inputStream, format, framesToRead);

        try {
            AudioSystem.write(clippedStream, AudioFileFormat.Type.WAVE, outputFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            inputStream.close();
            wavFile.delete();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            clippedStream.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return outputFile;
    }


    private File convertToWav(File sourceMp3File) throws JavaLayerException {
        Converter converter = new Converter();
        String wavFilePath = sourceMp3File.getPath().replace(MP3_FORMAT, WAV_FORMAT);
        converter.convert(sourceMp3File.getPath(), wavFilePath);
        return new File(wavFilePath);
    }
}
