package swm.betterlife.antifragile.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.member.dto.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.dto.NicknameModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.ProfileImgModifyRequest;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDetailResponse findMemberByEmail(String email, LoginType loginType) {
        return MemberDetailResponse.from(memberRepository.getMember(email, loginType));
    }

    @Transactional
    public void modifyNickname(
        NicknameModifyRequest request, String email, LoginType loginType
    ) {
        Member findMember = memberRepository.getMember(email, loginType);
        findMember.updateNickname(request.nickname());
    }

    @Transactional
    public void modifyProfileImg(
        ProfileImgModifyRequest request, String email, LoginType loginType
    ) {
        Member findMember = memberRepository.getMember(email, loginType);
        findMember.updateNickname(request.profileImg());
    }
}
