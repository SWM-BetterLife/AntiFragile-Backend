package swm.betterlife.antifragile.domain.recentcontent.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.recentcontent.service.RecentContentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recent-contents")
public class RecentContentController {

    private final RecentContentService recentContentService;

    @PostMapping("/{contentId}")
    public ResponseBody<Void> addRecentContent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String contentId
    ) {
        recentContentService.addRecentContent(principalDetails.memberId(), contentId);
        return ResponseBody.ok();
    }
}
