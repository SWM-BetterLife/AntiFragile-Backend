package swm.betterlife.antifragile.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.auth.dto.request.PasswordModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.request.MemberProfileModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailInfoResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberInfoResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberProfileModifyResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberRemainNumberResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberStatusResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseBody<MemberInfoResponse> findMember(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.findMemberById(principalDetails.memberId()));
    }

    @GetMapping("/detail")
    public ResponseBody<MemberDetailInfoResponse> findMemberDetail(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.findMemberDetailById(principalDetails.memberId())
        );
    }

    @PostMapping("/profile")
    public ResponseBody<MemberProfileModifyResponse> modifyProfile(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart(required = false) MultipartFile profileImgFile,
        @RequestPart MemberProfileModifyRequest profileModifyRequest
    ) {
        return ResponseBody.ok(
            memberService.modifyProfile(
                principalDetails.memberId(), profileModifyRequest, profileImgFile
            )
        );
    }

    @GetMapping("/duplication-check")
    public ResponseBody<MemberNicknameDuplResponse> checkNicknameDuplication(
        @RequestParam String nickname
    ) {
        return ResponseBody.ok(memberService.getNicknameDuplication(nickname));
    }

    @GetMapping("/remain-recommend-number")
    public ResponseBody<MemberRemainNumberResponse> getRemainRecommendNumber(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.getRemainRecommendNumber(principalDetails.memberId()));
    }

    @GetMapping("/status")
    public ResponseBody<MemberStatusResponse> checkMemberStatus(
        @RequestParam String email, @RequestParam LoginType loginType
    ) {
        return ResponseBody.ok(
            memberService.checkMemberStatus(email, loginType)
        );
    }

    @PostMapping("/password")
    public ResponseBody<Void> modifyPassword(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody PasswordModifyRequest passwordModifyRequest
    ) {
        memberService.modifyPassword(
            principalDetails.email(), principalDetails.memberId(),
            principalDetails.loginType(), passwordModifyRequest
        );
        return ResponseBody.ok();
    }
}
