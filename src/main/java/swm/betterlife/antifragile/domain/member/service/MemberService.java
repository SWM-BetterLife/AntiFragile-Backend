package swm.betterlife.antifragile.domain.member.service;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
import swm.betterlife.antifragile.domain.member.dto.response.MemberRemainNumberResponse;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;
    private final MemberPointService memberPointService;

    @Transactional(readOnly = true)
    public MemberDetailResponse findMemberByEmail(String id) {
        Integer point = memberPointService.getPointByMemberId(id);
        return MemberDetailResponse.from(memberRepository.getMember(id), point);
    }

    @Transactional
    public void modifyNickname(NicknameModifyRequest request, String id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("nickname", request.nickname());

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public void modifyProfileImg(ProfileImgModifyRequest request, String id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("profileImgUrl", request.profileImg());

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }

        //todo: S3 이미지 변경

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

    public MemberRemainNumberResponse getRemainRecommendNumber(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        return MemberRemainNumberResponse.from(member.getRemainRecommendNumber());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetRemainRecommendNumber() {
        Query query = new Query();
        Update update = new Update().set("remainRecommendNumber", 3);

        mongoTemplate.updateMulti(query, update, Member.class);
    }
}
