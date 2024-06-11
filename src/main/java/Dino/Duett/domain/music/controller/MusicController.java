package Dino.Duett.domain.music.controller;

import Dino.Duett.config.security.AuthMember;
import Dino.Duett.domain.music.dto.MusicCreateRequest;
import Dino.Duett.domain.music.dto.MusicResponse;
import Dino.Duett.domain.music.service.MusicService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/musics")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @Operation(summary = "마이페이지 음악 목록 조회하기")
    @GetMapping
    public JsonBody<List<MusicResponse>> getMusics(@AuthenticationPrincipal final AuthMember authMember) {
        return JsonBody.of(HttpStatus.OK.value(), "마이페이지 음악 목록 조회 성공", musicService.getMusics(authMember.getId()));
    }

    @Operation(summary = "마이페이지 음악 등록하기")
    @PostMapping
    public JsonBody<MusicResponse> createMusics(@AuthenticationPrincipal final AuthMember authMember, @RequestBody final MusicCreateRequest musicCreateRequest) {
        return JsonBody.of(HttpStatus.OK.value(), "마이페이지 음악 목록 조회 성공", musicService.createMusic(authMember.getId(), musicCreateRequest));
    }
}
