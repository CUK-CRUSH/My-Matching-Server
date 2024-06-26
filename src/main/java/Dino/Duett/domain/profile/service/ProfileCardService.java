package Dino.Duett.domain.profile.service;

import Dino.Duett.domain.image.service.ImageService;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.mood.dto.response.MoodResponse;
import Dino.Duett.domain.music.dto.response.MusicResponse;
import Dino.Duett.domain.profile.dto.response.ProfileCardResponse;
import Dino.Duett.domain.profile.dto.response.ProfileCardSummaryResponse;
import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.profile.exception.ProfileException;
import Dino.Duett.domain.profile.repository.ProfileRepository;
import Dino.Duett.domain.tag.enums.TagType;
import Dino.Duett.domain.tag.service.ProfileTagService;
import Dino.Duett.global.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import static Dino.Duett.global.enums.LimitConstants.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileCardService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final ProfileTagService profileTagService;
    private final ProfileUnlockService profileUnlockService;

    /**
     * 프로필 카드 완성 여부 조회
     * @param memberId 사용자 id
     * @return boolean
     */
    public boolean getProfileCardCompletionStatus(final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile profile = validateProfileIsNull(member.getProfile());
        return isProfileComplete(profile);
    }

    /**
     * 프로필 카드 요약 조회
     * @param memberId 사용자 id
     * @param page 페이지
     * @param size 사이즈
     * @param radius 반경(km)
     * @return List<ProfileCardSummaryResponse>
     */

    public List<ProfileCardSummaryResponse> getProfileCardsOfSummary(final Long memberId,
                                                                     final int page,
                                                                     final int size,
                                                                     double radius) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile viewerProfile = validateProfileIsNull(member.getProfile());
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Profile> profiles = profileRepository.findAllUsersWithinRadius(
                member.getProfile().getLocation().getLatitude(),
                member.getProfile().getLocation().getLatitude(),
                radius,
                member.getProfile().getGender().getOppositeGender(),
                viewerProfile.getProfileUnlocks() != null ? viewerProfile.getProfileUnlocks().stream().map(profileUnlock -> profileUnlock.getViewedProfile().getId()).toList() : null,
                pageRequest
        );

        return profiles.stream()
                .map(profile -> ProfileCardSummaryResponse.builder()
                                .profileId(profile.getId())
                                .name(profile.getName())
                                .birthDate(profile.getBirthDate())
                                .mbti(profile.getMbti())
                                .oneLineIntroduction(profile.getOneLineIntroduction())
                                .distance(calculateDistance(member.getProfile(), profile))
                                .lifeMusics(profile.getMusics().stream().map(MusicResponse::of).toList())
                                .tags(profileTagService.getProfileTagsOnlyFeatured(profile.getId()))
                                .build())
                .toList();
    }

    /**
     * 프로필 카드 코인으로 조회
     * @param memberId 사용자 id
     * @param profileId 프로필 id
     * @return ProfileCardResponse
     */
    @Transactional
    public ProfileCardResponse getProfileCardWithCoin(final Long memberId,
                                                      final Long profileId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile viewerProfile = validateProfileIsNull(member.getProfile());
        Profile viewedProfile = profileRepository.findById(profileId).orElseThrow(ProfileException.ProfileNotFoundException::new);
        validateProfileIsComplete(viewerProfile);

        //member.updateCoin(COIN);//todo: mvp에서는 coin을 사용하지 않음
        profileUnlockService.createProfileUnlock(viewerProfile.getId(), viewedProfile.getId());

        return convertToDto(viewedProfile);
    }

    /**
     * 프로필 카드 단일 조회
     * @param memberId 사용자 id
     * @param profileId 조회할 프로필 id
     * @return ProfileCardResponse
     */

    public ProfileCardResponse getProfileCard(final Long memberId, final Long profileId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Profile viewerProfile = validateProfileIsNull(member.getProfile());
        Profile viewedProfile = profileRepository.findById(profileId).orElseThrow(ProfileException.ProfileNotFoundException::new);

        validateProfileIsLocked(viewerProfile, viewedProfile);

        return convertToDto(viewedProfile);
    }

    private void validateProfileIsLocked(final Profile viewerProfile, final Profile viewedProfile) {
        boolean isProfileUnlocked = viewerProfile.getProfileUnlocks().stream()
                .anyMatch(profileUnlock -> profileUnlock.getViewedProfile().getId().equals(viewedProfile.getId()));

        if (!isProfileUnlocked) {
            throw new ProfileException.ProfileForbiddenException();
        }
    }
    /**
     * 자신의 프로필 완성 여부 확인
     * @param profile 프로필
     * @return boolean
     */
    private boolean isProfileComplete(final Profile profile) {
        int introCount = 0;
        int musicTasteCount = 0;

        // 내 정보
        boolean info = !Validator.isNullOrBlank(profile.getName()) &&
                !Validator.isNullOrBlank(profile.getOneLineIntroduction());

        // 내 소개
        if (profile.getMbti() != null) {
            introCount++;
        }
        if (profileTagService.checkFeaturedProfileTagsCount(profile)) {
            introCount++;
        }
        if (!Validator.isNullOrBlank(profile.getSelfIntroduction())) {
            introCount++;
        }
        if (!Validator.isNullOrBlank(profile.getLikeableMusicTaste())) {
            introCount++;
        }
        boolean intro = introCount >= PROFILE_INTRO_MIN_SIZE.getLimit();


        // 음악 취향
        if (profile.getMusics().size() >= MUSIC_MAX_LIMIT.getLimit()) {
            musicTasteCount++;
        }
        if (profile.getMood() != null) {
            musicTasteCount++;
        }
        boolean musicTaste = musicTasteCount >= PROFILE_MUSIC_TASTE_MIN_SIZE.getLimit();

        return info && intro && musicTaste;
    }

    /**
     * 자신의 프로필 완성 여부 검증
     * @param profile 프로필
     * @return boolean
     */
    private void validateProfileIsComplete(final Profile profile) {
        if (!isProfileComplete(profile)) {
            throw new ProfileException.ProfileIncompleteException();
        }
    }


    /**
     * 프로필 소유자 검증
     * @param member 사용자
     * @param profile 프로필
     */
    private void validateProfileOwner(Member member, Profile profile) {
        if (!member.getProfile().getId().equals(profile.getId())) {
            throw new ProfileException.ProfileForbiddenException();
        }
    }

    /**
     * 두 프로필 간 거리 계산
     * @param profile1 프로필1
     * @param profile2 프로필2
     * @return double
     */
//    private double calculateDistance(final Profile profile1, final Profile profile2) {
//        double distance = Math.sqrt(
//                    Math.pow(profile1.getLocation().getLatitude() - profile2.getLocation().getLatitude(), 2) +
//                        Math.pow(profile1.getLocation().getLongitude() - profile2.getLocation().getLongitude(), 2)
//
//        );
//        DecimalFormat df = new DecimalFormat("#.##");
//        return Double.parseDouble(df.format(distance));
//    }
    private double calculateDistance(final Profile profile1, final Profile profile2) {
        final BigDecimal EARTH_RADIUS_KM = new BigDecimal("6371.0");

        double lat1 = profile1.getLocation().getLatitude();
        double lon1 = profile1.getLocation().getLongitude();
        double lat2 = profile2.getLocation().getLatitude();
        double lon2 = profile2.getLocation().getLongitude();

        // Convert latitude and longitude to radians
        BigDecimal lat1Rad = BigDecimal.valueOf(Math.toRadians(lat1));
        BigDecimal lon1Rad = BigDecimal.valueOf(Math.toRadians(lon1));
        BigDecimal lat2Rad = BigDecimal.valueOf(Math.toRadians(lat2));
        BigDecimal lon2Rad = BigDecimal.valueOf(Math.toRadians(lon2));

        // Calculate the differences in latitude and longitude
        BigDecimal deltaLat = lat2Rad.subtract(lat1Rad);
        BigDecimal deltaLon = lon2Rad.subtract(lon1Rad);

        // Apply the Haversine formula
        BigDecimal sinDeltaLatDiv2 = BigDecimal.valueOf(Math.sin(deltaLat.doubleValue() / 2));
        BigDecimal sinDeltaLonDiv2 = BigDecimal.valueOf(Math.sin(deltaLon.doubleValue() / 2));

        BigDecimal a = sinDeltaLatDiv2.multiply(sinDeltaLatDiv2)
                .add(BigDecimal.valueOf(Math.cos(lat1Rad.doubleValue()))
                        .multiply(BigDecimal.valueOf(Math.cos(lat2Rad.doubleValue())))
                        .multiply(sinDeltaLonDiv2).multiply(sinDeltaLonDiv2));

        BigDecimal sqrtA = BigDecimal.valueOf(Math.sqrt(a.doubleValue()));
        BigDecimal sqrtOneMinusA = BigDecimal.valueOf(Math.sqrt(1.0 - a.doubleValue()));
        BigDecimal c = BigDecimal.valueOf(2.0).multiply(BigDecimal.valueOf(Math.atan2(sqrtA.doubleValue(), sqrtOneMinusA.doubleValue())));

        // Calculate the distance by multiplying with Earth's radius
        BigDecimal distance = EARTH_RADIUS_KM.multiply(c, new MathContext(10, RoundingMode.HALF_UP));

        // Round to one decimal place
        distance = distance.setScale(1, RoundingMode.HALF_UP);

        return distance.doubleValue();
    }

    private Profile validateProfileIsNull(Profile profile) {
        if (profile == null) {
            throw new ProfileException.ProfileNotFoundException();
        }
        return profile;
    }

    private ProfileCardResponse convertToDto(Profile profile) {
        return ProfileCardResponse.builder()
                .profileId(profile.getId())
                .name(profile.getName())
                .birthDate(profile.getBirthDate())
                .mbti(profile.getMbti())
                .oneLineIntroduction(profile.getOneLineIntroduction())
                .profileImageUrl(profile.getProfileImage() != null ? imageService.getUrl(profile.getProfileImage()) : null)
                .musicTags(profileTagService.getProfileTags(profile.getId(), TagType.MUSIC))
                .hobbyTags(profileTagService.getProfileTags(profile.getId(), TagType.HOBBY))
                .lifeMusics(profile.getMusics().stream().map(MusicResponse::of).toList())
                .mood(MoodResponse.of(
                        profile.getMood().getTitle(),
                        profile.getMood().getArtist(),
                        profile.getMood().getMoodImage() != null ? imageService.getUrl(profile.getMood().getMoodImage()) : null))
                .selfIntroduction(profile.getSelfIntroduction())
                .likeableMusicTaste(profile.getLikeableMusicTaste())
                .build();
    }
}
