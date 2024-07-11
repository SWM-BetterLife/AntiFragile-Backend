package swm.betterlife.antifragile.domain.pointreceipt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.response.PagingResponse;
import swm.betterlife.antifragile.domain.member.service.MemberService;
import swm.betterlife.antifragile.domain.pointreceipt.dto.request.PointChargeRequest;
import swm.betterlife.antifragile.domain.pointreceipt.dto.response.PointChargeResponse;
import swm.betterlife.antifragile.domain.pointreceipt.dto.response.PointReceiptDetailResponse;
import swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceipt;
import swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceiptType;
import swm.betterlife.antifragile.domain.pointreceipt.repository.PointReceiptRepository;

@Service
@RequiredArgsConstructor
public class PointReceiptService {

    private final PointReceiptRepository pointReceiptRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public PagingResponse<PointReceiptDetailResponse> getAllOwnPointReceipts(
        String memberId, Pageable pageable
    ) {
        Page<PointReceipt> pointReceipts
            = pointReceiptRepository.findAllByMemberId(memberId, pageable);

        return PagingResponse.from(pointReceipts.map(PointReceiptDetailResponse::from));
    }

    @Transactional
    public PointChargeResponse addPointCharge(
        String memberId, PointChargeRequest pointChargeRequest
    ) {
        pointReceiptRepository.save(PointReceipt.builder()
                .memberId(memberId)
                .type(PointReceiptType.CHARGE)
                .amount(pointChargeRequest.chargeAmount())
                .build());
        int chargedPoint
            = memberService.addPointByAmount(memberId, pointChargeRequest.chargeAmount());
        return new PointChargeResponse(chargedPoint);
    }

}
