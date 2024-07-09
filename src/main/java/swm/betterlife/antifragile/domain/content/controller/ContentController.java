package swm.betterlife.antifragile.domain.content.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            contentService.saveRecommendContents(
                principalDetails.email(),
                principalDetails.loginType(),
                date
            ));
    }
}
