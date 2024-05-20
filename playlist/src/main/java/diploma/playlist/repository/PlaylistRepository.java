package diploma.playlist.repository;

import diploma.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findPlaylistByYandexPlaylistIdAndUsername(String yandexPlaylistId, String username);

    List<Playlist> findAllByUsername(String username);
}
