package Dino.Duett.domain.music.entity;

import Dino.Duett.domain.music.dto.MusicCreateRequest;
import Dino.Duett.domain.music.dto.MusicUpdateRequest;
import Dino.Duett.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "music")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Music extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private String url;

    public Music(Long id, String title, String artist, String url) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.url = url;
    }

    public static Music of(String title, String artist, String url) {
        return new Music(
                null,
                title,
                artist,
                url
        );
    }

    public void updateMusic(MusicUpdateRequest musicUpdateRequest) {
        if(title != null && !title.isEmpty()) {
            this.title = musicUpdateRequest.getTitle();
        }
        if(artist != null && !artist.isEmpty()) {
            this.artist = musicUpdateRequest.getArtist();
        }
        if (url != null && !url.isEmpty()) {
            this.url = musicUpdateRequest.getUrl();
        }
    }
}
