package diploma.track.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import diploma.track.autnentication.AuthenticationFacade;
import diploma.track.util.TrackDirectoryUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static diploma.track.util.AudioFormatConstants.MP3_FORMAT;
import static diploma.track.util.TrackDirectoryUtils.*;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;
    private final AuthenticationFacade authenticationFacade;

    public byte[] downloadTrackToByteArray(String trackId) {
        String bucketName = authenticationFacade.getAuthentication().getName();
        S3Object s3Object = s3Client.getObject(bucketName, trackId);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IoUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public byte[] downloadTrackCover(String trackId) {
        S3Object object = s3Client.getObject("track-covers", trackId + "-cover.jpg");
        return object.getObjectContent().readAllBytes();
    }

    public File downloadTrackToFile(String trackId) {
        String username = authenticationFacade.getAuthentication().getName();
        File userTracksDir = new File(TRACKS_DIR + File.separator + username);
        if (!userTracksDir.exists()) userTracksDir.mkdir();
        byte[] trackData = downloadTrackToByteArray(trackId);
        return convertByteArrayToFile(trackData, new File(TRACKS_DIR + File.separator + username + File.separator + trackId));
    }

    private File convertByteArrayToFile(byte[] data, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
