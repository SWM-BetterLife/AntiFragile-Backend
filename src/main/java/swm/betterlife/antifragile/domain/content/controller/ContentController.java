package swm.betterlife.antifragile.domain.content.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.content.dto.response.ContentRecommendResponse;
import swm.betterlife.antifragile.domain.content.service.ContentService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;

    @PostMapping
    public ResponseBody<ContentRecommendResponse> addRecommendContents(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("date") LocalDate date
    ) {
        return ResponseBody.ok(
            contentService.saveRecommendContents(principalDetails.memberId(), date));
    }

    @PostMapping("/re")
    public ResponseBody<ContentRecommendResponse> addReRecommendContents(
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

    @PostMapping("/{contentId}/unlike")
    public ResponseBody<Void> unlikeContent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String contentId
    ) {
        contentService.unlikeContent(principalDetails.memberId(), contentId);
        return ResponseBody.ok();
    }
}
