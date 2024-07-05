package swm.betterlife.antifragile.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public Member findMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails :  {}", principalDetails);
        return memberService.findMemberByEmail(principalDetails.email());
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
