package diploma.track.repository;

import diploma.track.entity.TrackTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackTaskRepository extends JpaRepository<TrackTask, Integer> {

    List<TrackTask> findAllByTrackIdAndUsername(String trackId, String username);
}
