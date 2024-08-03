package swm.betterlife.antifragile.domain.emoticontheme.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.PagingResponse;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonInfoFromEmotionResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeSummaryResponse;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;
import swm.betterlife.antifragile.domain.emoticontheme.service.EmoticonThemeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emoticon-themes")
public class EmoticonThemeController {

    private final EmoticonThemeService emoticonThemeService;

    @GetMapping
    public ResponseBody<PagingResponse<EmoticonThemeSummaryResponse>> getEntireEmoticonThemes(
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseBody.ok(emoticonThemeService.getAllEmoticonThemes(pageable));
    }

    @GetMapping("/own")
    public ResponseBody<EmoticonThemeOwnEntireResponse> getEntireOwnEmoticonThemes(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String memberId = principalDetails.memberId();
        return ResponseBody.ok(emoticonThemeService.getAllOwnEmoticonThemes(memberId));
    }

    @GetMapping("/{emoticonThemeId}/emoticons")
    public ResponseBody<EmoticonEntireResponse> getEntireEmoticonsFromEmoticonTheme(
        @PathVariable String emoticonThemeId
    ) {
        return ResponseBody.ok(
            emoticonThemeService.getAllEmoticonsByEmoticonThemeId(emoticonThemeId)
        );
    }

    @GetMapping("/emoticons/{emotion}")
    public ResponseBody<List<EmoticonInfoFromEmotionResponse>> getEntireEmoticonsFromEmotion(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable Emotion emotion
    ) {
        return ResponseBody.ok(
            emoticonThemeService.getAllEmoticonsForAllThemesByEmotion(
                principalDetails.memberId(), emotion
            )
        );
    }

    @PostMapping("/{emoticonThemeId}/purchase")
    public ResponseBody<Void> purchaseEmoticonTheme(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable String emoticonThemeId
    ) {
        emoticonThemeService    // todo: 몽고 db 쿼리로 $push 로 해결하기.
            .addMemberIdToEmoticonTheme(principalDetails.memberId(), emoticonThemeId);
        return ResponseBody.ok();
    }

}
