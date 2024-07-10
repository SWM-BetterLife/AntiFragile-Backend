package swm.betterlife.antifragile.domain.emoticontheme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.service.EmoticonThemeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emoticon-themes")
public class EmoticonThemeController {

    private final EmoticonThemeService emoticonThemeService;

    @GetMapping
    public ResponseBody<EmoticonThemeEntireResponse> getEntireEmoticonThemes() {
        return ResponseBody.ok(emoticonThemeService.getAllEmoticonThemes());
    }

    @GetMapping("/own")
    public ResponseBody<EmoticonThemeOwnEntireResponse> getEntireOwnEmoticonThemes(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ObjectId memberId = principalDetails.memberId();
        return ResponseBody.ok(emoticonThemeService.getAllOwnEmoticonThemes(memberId));
    }

    @GetMapping("/{emoticonThemeId}/emoticons")
    public ResponseBody<EmoticonEntireResponse> getEntireEmoticons(
        @PathVariable ObjectId emoticonThemeId
    ) {
        return ResponseBody.ok(emoticonThemeService.getAllEmoticons(emoticonThemeId));
    }

    @PostMapping("/{emoticonThemeId}/purchase")
    public ResponseBody<Void> purchaseEmoticonTheme(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable ObjectId emoticonThemeId
    ) {
        emoticonThemeService
            .saveMemberIdAtEmoticonTheme(principalDetails.memberId(), emoticonThemeId);
        return ResponseBody.ok();
    }

}
