package Dino.Duett.domain.profile.entity;

import Dino.Duett.domain.image.entity.Image;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.mood.entity.Mood;
import Dino.Duett.domain.music.entity.Music;
import Dino.Duett.domain.profile.dto.request.ProfileInfoRequest;
import Dino.Duett.domain.profile.dto.request.ProfileIntroRequest;
import Dino.Duett.domain.profile.enums.GenderType;
import Dino.Duett.domain.profile.enums.MbtiType;
import Dino.Duett.domain.tag.dto.request.TagRequest;
import Dino.Duett.domain.tag.entity.ProfileTag;
import Dino.Duett.global.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.Multipart;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;
    @Column(length = 15)
    private String name;
    private LocalDateTime birthDate;
    @Enumerated(EnumType.STRING)
    private MbtiType mbti;
    @Column(length = 30)
    private String oneLineIntroduction;
    @Column(length = 500)
    private String selfIntroduction;
    private String likeableMusicTaste;
    @Enumerated(EnumType.STRING)
    private GenderType gender;
    @Embedded
    private Region region;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTag> profileTags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Mood mood;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Music> musics = new ArrayList<>();

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Profile(Long id, String name, LocalDateTime birthDate, MbtiType mbti, String oneLineIntroduction, String selfIntroduction, String likeableMusicTaste, GenderType gender, Region region, Image profileImage, List<ProfileTag> profileTags, Mood mood, List<Music> musics, Member member) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.mbti = mbti;
        this.oneLineIntroduction = oneLineIntroduction;
        this.selfIntroduction = selfIntroduction;
        this.likeableMusicTaste = likeableMusicTaste;
        this.gender = gender;
        this.region = region;
        this.profileImage = profileImage;
        this.profileTags = profileTags;
        this.mood = mood;
        this.musics = musics;
        this.member = member;
    }

    public void updateMood(final Mood mood) {
        this.mood = mood;
    }

    public void updateProfileInfo(final Image image, final String name, final String oneLineIntroduction) {
        if (image != null) {
            this.profileImage = image;
        }
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
        if (oneLineIntroduction != null && !oneLineIntroduction.isEmpty()) {
            this.oneLineIntroduction = oneLineIntroduction;
        }
    }
    public void updateProfileIntro(final MbtiType mbti, final String selfIntroduction, final String likeableMusicTaste) {
        if (mbti != null) {
            this.mbti = mbti;
        }
        if (selfIntroduction != null && !selfIntroduction.isEmpty()) {
            this.selfIntroduction = selfIntroduction;
        }
        if (likeableMusicTaste != null && !likeableMusicTaste.isEmpty()) {
            this.likeableMusicTaste = likeableMusicTaste;
        }
    }
}