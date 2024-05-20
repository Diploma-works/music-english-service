package diploma.playlist.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PlaylistIdHandler {

    public String extractPlaylistId(String url) {
        Pattern pattern = Pattern.compile("/playlists/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
