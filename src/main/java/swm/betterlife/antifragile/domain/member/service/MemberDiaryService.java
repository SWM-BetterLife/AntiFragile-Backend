package swm.betterlife.antifragile.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.member.repository.MemberDiaryRepository;

@Service
@RequiredArgsConstructor
public class MemberDiaryService {

    private final MemberDiaryRepository memberDiaryRepository;

    @Transactional(readOnly = true)
    public Integer getDiaryTotalNumByMemberId(String memberId) {
        return memberDiaryRepository.getMemberPoint(memberId).getDiaryTotalNum();
    }

}
