package Dino.Duett.domain.tag.service;

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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static Dino.Duett.global.enums.LimitConstants.TAG_MAX_LIMIT;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ProfileTagRepository profileTagRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    protected void init() {
        for (TagType type : TagType.values()) {
            for (String name : type.getNames()) {
                if (!tagRepository.existsByNameAndType(name, type.getType())) {
                    tagRepository.save(Tag.of(name, type.getType())
                    );
                }
            }
        }
    }

    public TagByTypeResponse getAllTags(final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = member.getProfile();

        return TagByTypeResponse.of(
                getAllTagsByTagType(profile, TagType.MUSIC),
                getAllTagsByTagType(profile, TagType.HOBBY));
    }

    public List<TagResponse> getAllTagsByTagType(final Profile profile, final TagType tagType) {
        List<Tag> tags = (tagType == TagType.MUSIC) ? tagRepository.findAllByType(TagType.MUSIC.getType()) : tagRepository.findAllByType(TagType.HOBBY.getType());
        List<ProfileTag> profileTags = profile.getProfileTags().stream().filter(pt -> pt.getTag().getType().equals(tagType.getType())).toList();

        return tags.stream()
                .map(tag -> {
                    int priority = profileTags.stream()
                            .anyMatch(pt -> pt.getTag().getId().equals(tag.getId()))
                            ? PriorityType.STANDARD.getKey()
                            : PriorityType.NONE.getKey();
                    return TagResponse.of(tag, priority);
                })
                .collect(Collectors.toList());
    }

    public List<TagResponse> getProfileTag(final Profile profile, final TagType tagType){
        return profile.getProfileTags().stream()
                    .filter(profileTag -> profileTag.getTag().getType().equals(tagType.getType()))
                    .map(profileTag -> TagResponse.of(profileTag.getTag(), profileTag.getPriority().getKey()))
                    .toList();
    }

    @Transactional
    public void changeTagsToProfile(final Long memberId, final List<TagRequest> musicTags, final List<TagRequest> hobbyTags) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = member.getProfile();
        changeTagsToProfileByTagType(profile, musicTags, TagType.MUSIC);
        changeTagsToProfileByTagType(profile, hobbyTags, TagType.HOBBY);
    }

    @Transactional
    public void changeTagsToProfileByTagType(final Profile profile, final List<TagRequest> tagRequests, final TagType tagType) {
        List<ProfileTag> profileTags = tagRequests.stream()
                .map(tagRequest -> processTagRequest(profile, tagRequest, tagType))
                .toList();

        saveOrUpdateProfileTags(profile, profileTags);
        validateTagLimit(profile, TagType.MUSIC);
        validateTagLimit(profile, TagType.HOBBY);
    }

    private ProfileTag processTagRequest(final Profile profile, final TagRequest tagRequest, final TagType tagType) {
        Tag tag = tagRepository.findByNameAndType(tagRequest.getName(), tagType.getType())
                .orElseThrow(TagException.TagNotFoundException::new);

        profile.getProfileTags().stream()
                .filter(pt -> pt.getTag().getId().equals(tag.getId()))
                .findFirst()
                .ifPresentOrElse(
                        existingProfileTag -> updateTagPriorityIfNecessary(existingProfileTag, tagRequest),
                        () -> addNewProfileTag(profile, tagRequest, tag)
                );

        return ProfileTag.of(PriorityType.findByPriorityType(tagRequest.getPriority()), tag);
    }

    private void updateTagPriorityIfNecessary(final ProfileTag existingProfileTag, final TagRequest tagRequest) {
        if (existingProfileTag.getPriority().getKey() != tagRequest.getPriority()) {
            existingProfileTag.updatePriority(PriorityType.findByPriorityType(tagRequest.getPriority()));
        }
    }

    private void addNewProfileTag(final Profile profile, final TagRequest tagRequest, final Tag tag) {
        ProfileTag newProfileTag = ProfileTag
                .of(PriorityType.findByPriorityType(tagRequest.getPriority()), tag);
        profile.getProfileTags().add(newProfileTag);
    }

    private void saveOrUpdateProfileTags(final Profile profile, final List<ProfileTag> profileTags) {
        profileTags.forEach(profileTag -> {
            if (!profileTag.getPriority().equals(PriorityType.NONE)) {
                profileTagRepository.save(profileTag);
            } else {
                profile.getProfileTags().remove(profileTag);
                profileTagRepository.delete(profileTag);
            }
        });
    }

    private void validateTagLimit(final Profile profile, final TagType tagType) {
        if (profile.getProfileTags().stream().filter(pt -> pt.getTag().getType().equals(tagType.getType())).toList().size() > TAG_MAX_LIMIT.getLimit()) {
            throw new TagException.TagMaxLimitException();
        }
    }

}
