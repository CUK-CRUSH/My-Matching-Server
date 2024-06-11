package Dino.Duett.domain.profile.controller;

import Dino.Duett.config.security.AuthMember;
import Dino.Duett.domain.mood.dto.MoodCreateRequest;
import Dino.Duett.domain.mood.dto.MoodResponse;
import Dino.Duett.domain.mood.service.MoodService;
import Dino.Duett.domain.music.dto.MusicCreateRequest;
import Dino.Duett.domain.music.dto.MusicResponse;
import Dino.Duett.domain.music.service.MusicService;
import Dino.Duett.domain.profile.dto.request.ProfileInfoRequest;
import Dino.Duett.domain.profile.dto.request.ProfileIntroRequest;
import Dino.Duett.domain.profile.dto.request.ProfileMusicRequest;
import Dino.Duett.domain.profile.dto.response.*;
import Dino.Duett.domain.profile.service.ProfileService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {

    private final ProfileService profileService;
    private final MusicService musicService;
    private final MoodService moodService;

    /* Profile Card */

//    @Operation(summary = "프로필카드 상세 단일 조회하기", tags = {"프로필카드"}) //todo : delete 예정
//    @GetMapping("/profiles/{profileId}")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "프로필카드 조회 성공"),
//            @ApiResponse(responseCode = "3000", description = "프로필카드 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
//    })
//    public JsonBody<ProfileCardDetailResponse> getProfileCardOfDetail(@RequestParam final Long memberId, //todo :memeber id -> current member로
//                                                                      @PathVariable final Long profileId){
//        ProfileCardDetailResponse profileCardDetailResponse = profileService.getProfileCardOfDetail(memberId, profileId);
//        return JsonBody.of(HttpStatus.OK.value(),"프로필카드 상세 단일 조회 성공", profileCardDetailResponse);
//    }

    @Operation(summary = "내 프로필 카드 조회하기", tags = {"프로필카드"})
    @GetMapping("/users/profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필카드 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
    })
    public JsonBody<ProfileCardResponse> getProfileCard(@AuthenticationPrincipal final AuthMember authMember){
        return JsonBody.of(HttpStatus.OK.value(), "내 프로필 카드 조회 성공", profileService.getProfileCard(authMember.getId()));
    }

    @Operation(summary = "코인을 사용해서 프로필카드 상세 단일 조회하기", tags = {"프로필카드"})
    @GetMapping("/profiles/{profileId}/coin")
            @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필카드 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "5002", description = "프로필 카드를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2005", description = "코인 부족", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileCardResponse> getProfileCardOfDetailWithCoin(@AuthenticationPrincipal AuthMember authMember,
                                                                        @PathVariable final Long profileId,
                                                                        @RequestParam final int coin){
        return JsonBody.of(HttpStatus.OK.value(),"코인을 사용해서 프로필카드 상세 단일 조회 성공", profileService.getProfileCardWithCoin(authMember.getId(), profileId, coin));
    }


    @Operation(summary = "반경 내의 프로필카드 요약 목록 조회하기", tags = {"프로필카드"})
    @GetMapping("/profiles/summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필카드 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
    })
    public JsonBody<List<ProfileCardSummaryResponse>> getProfileCardsOfSummary(@AuthenticationPrincipal AuthMember authMember,
                                                                               @RequestParam final int page,
                                                                               @RequestParam final int size,
                                                                               @RequestParam final double radius){
        return JsonBody.of(HttpStatus.OK.value(), "반경 내의 프로필카드 요약 목록 조회 성공", profileService.getProfileCardsOfSummary(authMember.getId(), page, size, radius));
    }


    /* My Page */

    @Operation(summary = "자신의 음악 취향(인생곡과 무드) 조회", tags = {"프로필카드"})
    @GetMapping("/profiles/music-taste")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileMusicResponse> getProfileMusicAndMood(@AuthenticationPrincipal final AuthMember authMember){
        return JsonBody.of(HttpStatus.OK.value(), "자신의 음악 취향(인생곡과 무드) 조회", profileService.getProfileMusic(authMember.getId()));
    }

    @Operation(summary = "자신의 무드 등록하기", tags = {"프로필카드"})
    @PostMapping("/profiles/mood")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<MoodResponse> createMood(@AuthenticationPrincipal final AuthMember authMember,
                                             @ModelAttribute final MoodCreateRequest moodCreateRequest){
        return JsonBody.of(HttpStatus.OK.value(), "자신의 무드 등록하기", moodService.createMood(authMember.getId(), moodCreateRequest));
    }

    @Operation(summary = "자신의 음악 등록하기", tags = {"프로필카드"})
    @PatchMapping("/profiles/music")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<MusicResponse> createMusic(@AuthenticationPrincipal final AuthMember authMember,
                                               @RequestBody final MusicCreateRequest musicCreateRequest){
        return JsonBody.of(HttpStatus.OK.value(), "자신의 음악 등록하기", musicService.createMusic(authMember.getId(), musicCreateRequest));
    }

    @Operation(summary = "자신의 음악 취향(인생곡과 무드) 수정하기", tags = {"프로필카드"})
    @PatchMapping("/profiles/music-taste")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileMusicResponse> updateProfileMusicAndMood(@AuthenticationPrincipal final AuthMember authMember,
                                                                    @ModelAttribute final ProfileMusicRequest profileMusicRequest){
        return JsonBody.of(HttpStatus.OK.value(), "자신의 음악 취향(인생곡과 무드) 수정하기", profileService.updateProfileMusic(authMember.getId(), profileMusicRequest));
    }

    @Operation(summary = "자신의 음악 삭제하기", tags = {"프로필카드"})
    @DeleteMapping("/profiles/music/{musicId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "6000", description = "음악을 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))

    })
    public JsonBody<Long> deleteMusic(@AuthenticationPrincipal final AuthMember authMember,
                                        @PathVariable final Long musicId){
        musicService.deleteMusic(authMember.getId() , musicId);
        return JsonBody.of(HttpStatus.OK.value(), "자신의 음악 삭제하기", musicId);
    }

    @Operation(summary = "내 정보 조회하기", tags = {"프로필카드"})
    @GetMapping("/profiles/info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileInfoResponse> getProfile(@AuthenticationPrincipal final AuthMember authMember){
        return JsonBody.of(HttpStatus.OK.value(), "내 정보 조회하기", profileService.getProfileInfo(authMember.getId()));
    }

    @Operation(summary = "내 정보 등록 및 수정하기", tags = {"프로필카드"})
    @PatchMapping("/profiles/info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileInfoResponse> updateProfile(@AuthenticationPrincipal final AuthMember authMember,
                                                       @RequestBody final ProfileInfoRequest profileInfoRequest){
        return JsonBody.of(HttpStatus.OK.value(), "내 정보 수정하기", profileService.updateProfileInfo(authMember.getId(), profileInfoRequest));
    }

    @Operation(summary = "내 소개 조회하기", tags = {"프로필카드"})
    @GetMapping("/profiles/intro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileIntroResponse> getProfileIntro(@AuthenticationPrincipal final AuthMember authMember){
        return JsonBody.of(HttpStatus.OK.value(), "내 소개 조회하기", profileService.getProfileIntro(authMember.getId()));
    }

    @Operation(summary = "내 소개 등록 및 수정하기", tags = {"프로필카드"})
    @PatchMapping("/profiles/intro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "음악 취향 조회 성공"),
            @ApiResponse(responseCode = "403", description = "비허가된 유저의 접근", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<ProfileIntroResponse> updateProfileIntro(@AuthenticationPrincipal final AuthMember authMember,
                                                            @RequestBody final ProfileIntroRequest profileIntroRequest){
        return JsonBody.of(HttpStatus.OK.value(), "내 소개 수정하기", profileService.updateProfileIntro(authMember.getId(), profileIntroRequest));
    }

    @GetMapping("/test") //todo : delete 예정
    public ResponseEntity<?> getTest(){
        profileService.test();
        return ResponseEntity.ok().build();
    }
}
