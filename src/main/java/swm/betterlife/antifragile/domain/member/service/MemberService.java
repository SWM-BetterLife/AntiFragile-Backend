package swm.betterlife.antifragile.domain.member.service;

import static swm.betterlife.antifragile.common.util.S3ImageCategory.PROFILE;

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
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.exception.ExcessRecommendLimitException;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.common.util.S3ImageComponent;
import swm.betterlife.antifragile.domain.member.dto.request.NicknameModifyRequest;
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
    private final MemberDiaryService memberDiaryService;
    private final S3ImageComponent s3ImageComponent;

    @Transactional(readOnly = true)
    public MemberDetailResponse findMemberByEmail(String id) {
        Integer point = memberPointService.getPointByMemberId(id);
        Integer diaryTotalNum = memberDiaryService.getDiaryTotalNumByMemberId(id);
        return MemberDetailResponse
            .from(memberRepository.getMember(id), point, diaryTotalNum);
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
    public void modifyProfileImg(MultipartFile profileImgFile, String id) {

        Member member = memberRepository.getMember(id);
        String newProfileImgUrl = s3ImageComponent.uploadImage(PROFILE, profileImgFile);
        String originProfileImgUrl = member.getProfileImgUrl();
        if(originProfileImgUrl != null) {
            s3ImageComponent.deleteImage(originProfileImgUrl);
            // todo: S3 이미지 업로드 트랜잭션 관리 멘토님께 물어보기
        }

        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("profileImgUrl", newProfileImgUrl);

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
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
