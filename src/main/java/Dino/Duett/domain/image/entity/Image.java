package Dino.Duett.domain.image.entity;

import Dino.Duett.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "extension", nullable = false, length = 30)
    String extension;

    @Column(name = "uuid", nullable = false, length = 50)
    String uuid;

    @Builder
    public Image(Long id, String name, String extension, String uuid) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.uuid = uuid;
    }
}
