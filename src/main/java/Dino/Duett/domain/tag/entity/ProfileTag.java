package Dino.Duett.domain.tag.entity;

import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.tag.enums.PriorityType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_tag_id")
    private Long id;

    private PriorityType priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ProfileTag(final Long id, final PriorityType priority, final Tag tag){
        this.id = id;
        this.priority = priority;
        this.tag = tag;
    }

    public static ProfileTag of(final PriorityType priority, final Tag tag){
        return new ProfileTag(
                null,
                priority,
                tag
        );
    }
    public void updatePriority(final PriorityType priority){
        this.priority = priority;
    }
}
