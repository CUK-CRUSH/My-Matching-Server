package Dino.Duett.domain.tag.service;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.profile.exception.ProfileException;
import Dino.Duett.domain.tag.dto.request.TagRequest;
import Dino.Duett.domain.tag.dto.response.TagByTypeResponse;
import Dino.Duett.domain.tag.dto.response.TagResponse;
import Dino.Duett.domain.tag.entity.ProfileTag;
import Dino.Duett.domain.tag.entity.Tag;
import Dino.Duett.domain.tag.enums.PriorityType;
import Dino.Duett.domain.tag.enums.TagType;
import Dino.Duett.domain.tag.exception.TagException;
import Dino.Duett.domain.tag.repository.ProfileTagRepository;
import Dino.Duett.domain.tag.repository.TagRepository;
import Dino.Duett.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static Dino.Duett.global.enums.LimitConstants.TAG_MAX_LIMIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProfileTagRepository profileTagRepository;

    private Profile profile;
    private Tag musicTag;
    private Tag hobbyTag;
    private TagRequest tagRequest;
    private Member member;


    @BeforeEach
    void setUp() {
        member  = TestUtil.createMember();
        profile = Profile.builder().profileTags(new ArrayList<>()).build();

        musicTag = new Tag(
                1L,
                "Rock",
                TagType.MUSIC.getType()
        );
        hobbyTag = new Tag(
                2L,
                "명상",
                TagType.HOBBY.getType()
        );
        tagRequest = new TagRequest("Rock", PriorityType.STANDARD.getKey());
    }


    @Test
    void getAllTagsByTagType() {
        when(tagRepository.findAllByType(TagType.MUSIC.getType())).thenReturn(Arrays.asList(musicTag));
        profile.getProfileTags().add(ProfileTag.of(PriorityType.STANDARD, musicTag));

        List<TagResponse> tagResponses = tagService.getAllTagsByTagType(profile, TagType.MUSIC);

        assertEquals(1, tagResponses.size());
        assertEquals("Rock", tagResponses.get(0).getName());
        assertEquals(PriorityType.STANDARD.getKey(), tagResponses.get(0).getPriority());
    }

    @Test
    @DisplayName("프로필 태그 변경 테스트 - 체크")
    void changeTagsToProfileByTagType_check() {
        // given
        when(tagRepository.findByNameAndType("Rock", TagType.MUSIC.getType())).thenReturn(Optional.of(musicTag));
        ProfileTag profileTag = ProfileTag.of(PriorityType.STANDARD, musicTag);
        when(profileTagRepository.save(any(ProfileTag.class))).thenReturn(profileTag);
        TagRequest newTagRequest = new TagRequest("Rock", PriorityType.STANDARD.getKey());

        // when
        tagService.changeTagsToProfileByTagType(profile, Arrays.asList(newTagRequest), TagType.MUSIC);

        // then
        verify(profileTagRepository, times(1)).save(any(ProfileTag.class));

    }

    @Test
    @Disabled //todo: 태그 수정
    @DisplayName("프로필 태그 변경 테스트 - 언체크")
    void changeTagsToProfileByTagType_delete() {
        // given
        when(tagRepository.findByNameAndType("Rock", TagType.MUSIC.getType())).thenReturn(Optional.of(musicTag));
        ProfileTag existingProfileTag = ProfileTag.of(PriorityType.STANDARD, musicTag);
        profile.getProfileTags().add(existingProfileTag);
        TagRequest deleteTagRequest = new TagRequest("Rock", PriorityType.NONE.getKey());

        // when
        tagService.changeTagsToProfileByTagType(profile, Arrays.asList(deleteTagRequest), TagType.MUSIC);

        // then
        verify(profileTagRepository, times(1)).delete(any(ProfileTag.class));
        assertFalse(profile.getProfileTags().stream().anyMatch(pt -> pt.getTag().equals(musicTag)));
    }
}
