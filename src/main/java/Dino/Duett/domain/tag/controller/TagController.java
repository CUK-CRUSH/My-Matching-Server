package Dino.Duett.domain.tag.controller;

import Dino.Duett.config.security.AuthMember;
import Dino.Duett.domain.tag.dto.request.TagByTypeRequest;
import Dino.Duett.domain.tag.dto.response.TagByTypeResponse;
import Dino.Duett.domain.tag.service.TagService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 조회하기", tags = {"태그"})
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 태그 조회 성공"),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
               })
    public JsonBody<TagByTypeResponse> getAllTags(@AuthenticationPrincipal final AuthMember authMember) {
        return JsonBody.of(HttpStatus.OK.value(), "유저 태그 조회 성공", tagService.getAllTags(authMember.getId()));
    }

    @Operation(summary = "태그 등록 및 수정하기", tags = {"태그"})
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 태그 조회 성공"),
            @ApiResponse(responseCode = "2002", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "8002", description = "태그 개수 초과", content = @Content(schema = @Schema(hidden = true)))
    })
    public JsonBody<Void> changeTag(@AuthenticationPrincipal final AuthMember authMember,
                                                @RequestBody final TagByTypeRequest tagByTypeRequest) {
        tagService.changeTagsToProfile(authMember.getId(), tagByTypeRequest.getMusicTags(), tagByTypeRequest.getHobbyTags());
        return JsonBody.of(HttpStatus.OK.value(), "유저 태그 변환 성공", null);
    }
}
