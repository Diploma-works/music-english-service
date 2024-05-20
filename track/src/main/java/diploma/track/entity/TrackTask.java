package diploma.track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "track_task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @Column
    private Integer lyricNumber;

    @Column
    private String username;

    @Column
    private String playlistYandexId;
}
