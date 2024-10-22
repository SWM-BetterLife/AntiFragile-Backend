package swm.betterlife.antifragile.domain.member.service;

import static swm.betterlife.antifragile.common.util.S3ImageCategory.PROFILE;
import static swm.betterlife.antifragile.domain.member.dto.response.MemberStatusResponse.Status.EXISTENCE;
import static swm.betterlife.antifragile.domain.member.dto.response.MemberStatusResponse.Status.HUMAN;
import static swm.betterlife.antifragile.domain.member.dto.response.MemberStatusResponse.Status.NOT_EXISTENCE;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.exception.ExcessRecommendLimitException;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.common.exception.PasswordSameException;
import swm.betterlife.antifragile.common.util.S3ImageComponent;
import swm.betterlife.antifragile.domain.member.controller.MemberNicknameDuplResponse;
import swm.betterlife.antifragile.domain.member.dto.request.MemberProfileModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.request.PasswordModifyRequest;
import swm.betterlife.antifragile.domain.member.dto.response.MemberDetailInfoResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberInfoResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberProfileModifyResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberRemainNumberResponse;
import swm.betterlife.antifragile.domain.member.dto.response.MemberStatusResponse;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
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
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberById(String id) {
        Integer point = memberPointService.getPointByMemberId(id);
        Integer diaryTotalNum = memberDiaryService.getDiaryTotalNumByMemberId(id);
        Member member = memberRepository.getMember(id);
        return MemberInfoResponse.from(
            member, point, diaryTotalNum,
            s3ImageComponent.getUrl(member.getProfileImgFilename())
        );
    }

    @Transactional(readOnly = true)
    public MemberDetailInfoResponse findMemberDetailById(String id) {
        Member member = memberRepository.getMember(id);
        return MemberDetailInfoResponse
            .from(member, s3ImageComponent.getUrl(member.getProfileImgFilename()));
    }

    public Member getMemberById(String id) {
        return memberRepository.getMember(id);
    }

    @Transactional
    public MemberProfileModifyResponse modifyProfile(
        String id, MemberProfileModifyRequest request, MultipartFile profileImgFile
    ) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update()
            .set("nickname", request.nickname())
            .set("birthDate", request.birthDate())
            .set("gender", request.gender())
            .set("job", request.job());

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }

        String profileImgUrl = null;
        if (profileImgFile != null) {
            profileImgUrl = modifyProfileImg(profileImgFile, id);
        }

        return new MemberProfileModifyResponse(
            request.nickname(), request.birthDate(),
            request.gender(), request.job(), profileImgUrl
        );
    }

    private String modifyProfileImg(MultipartFile profileImgFile, String id) {
        Member member = memberRepository.getMember(id);
        String originFilename = member.getProfileImgFilename();
        if (originFilename != null) {
            s3ImageComponent.deleteImage(originFilename);
        }

        String newFilename = s3ImageComponent.uploadImage(PROFILE, profileImgFile);
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("profileImgFilename", newFilename);

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
        return s3ImageComponent.getUrl(newFilename);
    }

    @Transactional(readOnly = true)
    public MemberNicknameDuplResponse getNicknameDuplication(String nickname) {
        return new MemberNicknameDuplResponse(memberRepository.existsByNickname(nickname));
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

    @Transactional(readOnly = true)
    public MemberRemainNumberResponse getRemainRecommendNumber(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        return MemberRemainNumberResponse.from(member.getRemainRecommendNumber());
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse checkMemberStatus(
        String email, LoginType loginType
    ) {

        if (!memberRepository.existsByEmailAndLoginType(email, loginType)) {
            return new MemberStatusResponse(NOT_EXISTENCE);
        }

        Member member = memberRepository.getMember(email, loginType);

        return (member.getDeletedAt() != null)
            ? new MemberStatusResponse(HUMAN)
            : new MemberStatusResponse(EXISTENCE);

    }

    @Transactional
    public void modifyPassword(
        String memberId, PasswordModifyRequest passwordModifyRequest
    ) {
        String curPassword = passwordModifyRequest.curPassword();
        String newPassword = passwordModifyRequest.newPassword();
        if(curPassword.equals(newPassword)) throw new PasswordSameException();
        String encodedPassword = passwordEncoder.encode(newPassword);
        Query query = new Query(Criteria.where("id").is(memberId));
        Update update = new Update().set("password", encodedPassword);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetRemainRecommendNumber() {
        Query query = new Query();
        Update update = new Update().set("remainRecommendNumber", 3);

        mongoTemplate.updateMulti(query, update, Member.class);
    }

}
