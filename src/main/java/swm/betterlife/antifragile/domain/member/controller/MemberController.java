package swm.betterlife.antifragile.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.member.dto.request.NicknameModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberRemainNumberResponse;
import swm.betterlife.antifragile.domain.member.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseBody<MemberDetailResponse> findMember(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.findMemberByEmail(principalDetails.memberId()));
    }

    @PutMapping("/nickname")
    public ResponseBody<Void> modifyNickname(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody NicknameModifyRequest request
    ) {
        memberService.modifyNickname(request, principalDetails.memberId());
        return ResponseBody.ok();
    }

    @PutMapping("/profile-img")
    public ResponseBody<Void> modifyProfileImg(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart MultipartFile profileImgFile
    ) {
        memberService.modifyProfileImg(profileImgFile, principalDetails.memberId());
        return ResponseBody.ok();
    }

    @GetMapping("/re-recommend-number")
    public ResponseBody<MemberRemainNumberResponse> getRemainRecommendNumber(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.getRemainRecommendNumber(principalDetails.memberId()));
    }
}
