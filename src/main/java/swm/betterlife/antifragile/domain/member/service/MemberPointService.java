package swm.betterlife.antifragile.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.member.repository.MemberPointRepository;

@Service
@RequiredArgsConstructor
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;

    @Transactional(readOnly = true)
    public Integer getPointByMemberId(String memberId) {
        return memberPointRepository.getMemberPoint(memberId).getPoint();
    }

}
