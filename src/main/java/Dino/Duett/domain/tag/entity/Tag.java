package Dino.Duett.domain.tag.entity;

import Dino.Duett.domain.tag.enums.TagType;
import Dino.Duett.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    private String type;

    public Tag(final Long id, final String name, final String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public static Tag of(final String name, final String type) {
        return new Tag(
                null,
                name,
                type);
    }
}
