package diploma.track.util;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class TrackDirectoryUtils {

    public static final File ROOT_PROJECT_DIR = new File(System.getProperty("user.dir"));
    public static final File TRACK_PROJECT_DIR = new File(ROOT_PROJECT_DIR + File.separator + "track");
    public static final File TRACKS_DIR = new File(TRACK_PROJECT_DIR + File.separator + "tracks");

    static {
        if (!TRACKS_DIR.exists()) {
            TRACKS_DIR.mkdirs();
        }
    }
}
