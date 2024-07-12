package swm.betterlife.antifragile.domain.member.service;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.domain.member.dto.request.NicknameModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.request.ProfileImgModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;

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
        memberRepository.save(findMember);
    }

    @Transactional
    public void modifyProfileImg(
        ProfileImgModifyRequest request, String email, LoginType loginType
    ) {
        Member findMember = memberRepository.getMember(email, loginType);
        findMember.updateProfileImgUrl(request.profileImg()); //todo: S3 이미지 변경 코드 추가
        memberRepository.save(findMember);
    }

    @Transactional
    public Integer addPointByAmount(String memberId, Integer amount) {

        Query query = new Query(Criteria.where("_id").is(memberId));
        Update update = new Update().inc("point", amount);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Member.class);

        if (updateResult.getModifiedCount() == 0) {
            throw new MemberNotFoundException();
        } else {
            Member updatedMember = mongoTemplate.findOne(query, Member.class);
            return updatedMember.getPoint();
        }
    }

}
