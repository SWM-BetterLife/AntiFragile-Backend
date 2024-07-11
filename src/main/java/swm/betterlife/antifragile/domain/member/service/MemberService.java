package swm.betterlife.antifragile.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.ExcessRecommendLimitException;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.domain.member.dto.request.NicknameModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.request.ProfileImgModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MongoTemplate mongoTemplate;

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
        findMember.updateProfileImgUrl(request.profileImg()); //todo: S3 이미지 변경 코드 추가
    }

    public void decrementRemainRecommendNumber(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        if (member.getRemainRecommendNumber() <= 0) {
            throw new ExcessRecommendLimitException();
        }
        member.decrementRemainRecommendNumber();
        memberRepository.save(member);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetRemainRecommendNumber() {
        Query query = new Query();
        Update update = new Update().set("remainRecommendNumber", 3);

        mongoTemplate.updateMulti(query, update, Member.class);
    }
}
