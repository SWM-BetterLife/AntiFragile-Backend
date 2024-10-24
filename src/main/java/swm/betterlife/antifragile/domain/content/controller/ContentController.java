package swm.betterlife.antifragile.domain.content.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.content.dto.response.ContentDetailResponse;
import swm.betterlife.antifragile.domain.content.dto.response.ContentListResponse;
import swm.betterlife.antifragile.domain.content.service.ContentQueryService;
import swm.betterlife.antifragile.domain.content.service.ContentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;
    private final ContentQueryService contentQueryService;

    @PostMapping
    public ResponseBody<ContentListResponse> addRecommendContents(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("date") LocalDate date
    ) {
        return ResponseBody.ok(
            contentService.saveRecommendContents(principalDetails.memberId(), date));
    }

    @PostMapping("/re")
    public ResponseBody<ContentListResponse> addReRecommendContents(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("date") LocalDate date,
        @RequestParam("feedback") String feedback
    ) {
        return ResponseBody.ok(
            contentService.saveReRecommendContents(principalDetails.memberId(), date, feedback));
    }

    // TODO: 좋아요, 좋아요 취소 하나로 합칠 지 고민
    @PostMapping("/{contentId}/like")
    public ResponseBody<Void> likeContent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String contentId
    ) {
        contentService.likeContent(principalDetails.memberId(), contentId);
        return ResponseBody.ok();
    }

    @DeleteMapping("/{contentId}/unlike")
    public ResponseBody<Void> unlikeContent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String contentId
    ) {
        contentService.unlikeContent(principalDetails.memberId(), contentId);
        return ResponseBody.ok();
    }

    @GetMapping
    public ResponseBody<ContentListResponse> getRecommendContents(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("date") LocalDate date
    ) {
        return ResponseBody.ok(
            contentQueryService.getRecommendContents(principalDetails.memberId(), date));
    }

    @GetMapping("/{contentId}")
    public ResponseBody<ContentDetailResponse> getContentDetail(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String contentId
    ) {
        return ResponseBody.ok(
            contentQueryService.getContentDetail(principalDetails.memberId(), contentId));
    }
}
