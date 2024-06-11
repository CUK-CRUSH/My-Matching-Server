package Dino.Duett.domain.profile.service;

import Dino.Duett.domain.image.service.ImageService;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.mood.dto.MoodResponse;
import Dino.Duett.domain.mood.service.MoodService;
import Dino.Duett.domain.music.dto.MusicResponse;
import Dino.Duett.domain.music.service.MusicService;
import Dino.Duett.domain.profile.dto.request.ProfileInfoRequest;
import Dino.Duett.domain.profile.dto.request.ProfileIntroRequest;
import Dino.Duett.domain.profile.dto.request.ProfileMusicRequest;
import Dino.Duett.domain.profile.dto.response.*;
import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.profile.enums.MbtiType;
import Dino.Duett.domain.profile.exception.ProfileException;
import Dino.Duett.domain.profile.repository.ProfileRepository;
import Dino.Duett.domain.tag.enums.TagType;
import Dino.Duett.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final TagService tagService;
    private final MusicService musicService;
    private final MoodService moodService;
    private final ImageService imageService;

    @Transactional
    public ProfileCardResponse getProfileCard(final Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = member.getProfile();

        return ProfileCardResponse.of(
                profile,
                getDistance(profile.getMember().getProfile(), profile),
                imageService.getUrl(profile.getMood().getMoodImage()),
                MoodResponse.of(profile.getMood().getTitle(), profile.getMood().getArtist(), imageService.getUrl(profile.getMood().getMoodImage())),
                profile.getMusics().stream().map(MusicResponse::of).toList(),
                tagService.getProfileTag(profile, TagType.MUSIC),
                tagService.getProfileTag(profile, TagType.HOBBY)
        );
    }

    @Transactional
    public ProfileCardResponse getProfileCardWithCoin(final Long memberId,
                                                      final Long profileId,
                                                      final int coin){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileException.ProfileNotFoundException::new);
        validateProfileOwner(member, profile);

        //member.updateCoin(coin);//todo: mvp에서는 coin을 사용하지 않음

        return ProfileCardResponse.of(
                profile,
                getDistance(profile.getMember().getProfile(), profile),
                imageService.getUrl(profile.getMood().getMoodImage()),
                MoodResponse.of(profile.getMood().getTitle(), profile.getMood().getArtist(), imageService.getUrl(profile.getMood().getMoodImage())),
                profile.getMusics().stream().map(MusicResponse::of).toList(),
                tagService.getProfileTag(profile, TagType.MUSIC),
                tagService.getProfileTag(profile, TagType.HOBBY)
        );
    }
    private void validateProfileOwner(Member member, Profile profile) {
        if (!member.getProfile().getId().equals(profile.getId())) {
            throw new ProfileException.ProfileForbiddenException();
        }
    }

    public List<ProfileCardSummaryResponse> getProfileCardsOfSummary(final Long memberId,
                                                                     final int page,
                                                                     final int size,
                                                                     double radius) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Profile> profiles = profileRepository.findAllUsersWithinRadius(
                member.getProfile().getRegion().getLatitude(),
                member.getProfile().getRegion().getLatitude(),
                radius,
                member.getProfile().getGender().getOppositeGender(),
                pageRequest
        );

        return profiles.stream()
                .map(profile -> ProfileCardSummaryResponse.of(
                        profile,
                        getDistance(member.getProfile(), profile),
                        imageService.getUrl(profile.getProfileImage())
                ))
                .toList();
    }

    public double getDistance(final Profile profile1, final Profile profile2) {
        return Math.sqrt(Math.pow(profile1.getRegion().getLatitude() - profile2.getRegion().getLatitude(), 2) + Math.pow(profile1.getRegion().getLongitude() - profile2.getRegion().getLongitude(), 2));
    }

    public Profile createProfile(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = Profile.builder()
                .member(member)
                .build();
        return profileRepository.save(profile);
    }

    public ProfileMusicResponse getProfileMusic(final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());

        return ProfileMusicResponse.of(
                musicService.getMusics(memberId),
                moodService.getMood(memberId)
        );
    }

    private Profile validateProfileIsNull(Profile profile) {
        if (profile == null) {
            throw new ProfileException.ProfileNotFoundException();
        }
        return profile;
    }


    @Transactional
    public ProfileMusicResponse updateProfileMusic(final Long memberId, final ProfileMusicRequest profileMusicRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());

        return ProfileMusicResponse.of(
                musicService.updateMusics(memberId, profileMusicRequest.getLifeMusics()),
                moodService.updateMood(memberId, profile.getMood().getId(), profileMusicRequest.getMood())
                );
    }

    public ProfileInfoResponse getProfileInfo(final Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());
        return ProfileInfoResponse.of(
                profile,
                imageService.getUrl(profile.getProfileImage())
        );
    }

    public ProfileIntroResponse getProfileIntro(final Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());
        return ProfileIntroResponse.of(
                profile.getMbti().getValue(),
                tagService.getProfileTag(profile, TagType.MUSIC),
                tagService.getProfileTag(profile, TagType.HOBBY),
                profile.getSelfIntroduction(),
                profile.getLikeableMusicTaste()

        );
    }

    @Transactional
    public ProfileIntroResponse updateProfileIntro(final Long memberId, final ProfileIntroRequest profileIntroRequest){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());

        profile.updateProfileIntro(
                MbtiType.findByMbtiType(profileIntroRequest.getMbti()),
                profileIntroRequest.getSelfIntroduction(),
                profileIntroRequest.getLikeableMusicTaste()
        );
        tagService.changeTagsToProfile(memberId, profileIntroRequest.getMusicTags(), profileIntroRequest.getHobbyTags());
        return ProfileIntroResponse.of(
                profile.getMbti().getValue(),
                tagService.getProfileTag(profile, TagType.MUSIC),
                tagService.getProfileTag(profile, TagType.HOBBY),
                profile.getSelfIntroduction(),
                profile.getLikeableMusicTaste()
        );
    }

    public ProfileInfoResponse updateProfileInfo(final Long memberId, final ProfileInfoRequest profileInfoRequest){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());
        if (profileInfoRequest.getProfileImage() != null && profile.getProfileImage() != null) {
            if(profileInfoRequest.getIsDeleteProfileImage()) {
                imageService.deleteImage(profile.getProfileImage().getId());
            }
        }

        imageService.deleteImage(profile.getProfileImage().getId());
        profile.updateProfileInfo(
                imageService.saveImage(profileInfoRequest.getProfileImage()),
                profileInfoRequest.getName(),
                profileInfoRequest.getOneLineIntroduction()
        );

        return ProfileInfoResponse.of(
                profile,
                imageService.getUrl(profile.getProfileImage())
        );
    }

    public void test(){
        Member member = Member.builder()
                .phoneNumber("01012345678")
                .kakaoId("kakaoId")
                .coin(0)
                .state(MemberState.ACTIVE)
                .build();
        memberRepository.save(member);
    }
}
