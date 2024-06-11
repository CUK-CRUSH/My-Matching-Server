package Dino.Duett.domain.mood.service;

import Dino.Duett.domain.image.service.ImageService;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.mood.dto.MoodCreateRequest;
import Dino.Duett.domain.mood.dto.MoodResponse;
import Dino.Duett.domain.mood.dto.MoodUpdateRequest;
import Dino.Duett.domain.mood.entity.Mood;
import Dino.Duett.domain.mood.exception.MoodException;
import Dino.Duett.domain.mood.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoodService {
    private final MoodRepository moodRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    public MoodResponse getMood(final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        Mood mood = member.getProfile().getMood();

        return MoodResponse.of(
                mood.getTitle(),
                mood.getArtist(),
                imageService.getUrl(mood.getMoodImage())
        );
    }

    @Transactional
    public MoodResponse createMood(final Long memberId, final MoodCreateRequest moodCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);

        Mood mood = Mood.of(
                moodCreateRequest.getTitle(),
                moodCreateRequest.getArtist(),
                imageService.saveImage(moodCreateRequest.getMoodImage())
        );
        moodRepository.save(mood);
        member.getProfile().updateMood(mood);

        return MoodResponse.of(
                mood.getTitle(),
                mood.getArtist(),
                imageService.getUrl(mood.getMoodImage())
        );
    }
//    public Mood createMoodToProfile(final Long memberId, final MoodCreateRequest moodCreateRequest) {
//        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
//
//        Mood mood = Mood.of(
//                moodCreateRequest.getTitle(),
//                moodCreateRequest.getArtist(),
//                imageService.saveImage(moodCreateRequest.getMoodImage())
//        );
//        moodRepository.save(mood);
//        return mood;
//    }

    @Transactional
    public MoodResponse updateMood(final Long memberId, final Long moodId, final MoodUpdateRequest moodUpdateRequest) {
        Mood mood = moodRepository.findById(moodId).orElseThrow(MoodException.MoodNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MemberNotFoundException::new);
        if(!member.getProfile().getMood().equals(mood)){
            throw new MoodException.MoodForbiddenException();
        }
        if(mood.getMoodImage() != null && moodUpdateRequest.getIsDeleteMoodImage()){
            imageService.deleteImage(mood.getMoodImage().getId());
        }
        mood.updateMood(
                moodUpdateRequest.getTitle(),
                moodUpdateRequest.getArtist(),
                imageService.saveImage(moodUpdateRequest.getMoodImage())
        );
        return MoodResponse.of(
                mood.getTitle(),
                mood.getArtist(),
                imageService.getUrl(mood.getMoodImage())
        );
    }
}
