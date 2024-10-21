package swm.betterlife.antifragile.domain.pointreceipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.PagingResponse;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.pointreceipt.dto.request.PointChargeRequest;
import swm.betterlife.antifragile.domain.pointreceipt.dto.response.PointChargeResponse;
import swm.betterlife.antifragile.domain.pointreceipt.dto.response.PointReceiptDetailResponse;
import swm.betterlife.antifragile.domain.pointreceipt.service.PointReceiptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point-receipts")
public class PointReceiptController {

    private final PointReceiptService pointReceiptService;

    @GetMapping()
    public ResponseBody<PagingResponse<PointReceiptDetailResponse>> getOwnEntirePointReceipts(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseBody.ok(
            pointReceiptService.getAllOwnPointReceipts(principalDetails.memberId(), pageable)
        );
    }

    @PostMapping("/charge")
    public ResponseBody<PointChargeResponse> chargePoint(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody PointChargeRequest pointChargeRequest
    ) {
        return ResponseBody.ok(
            pointReceiptService.addPointCharge(principalDetails.memberId(), pointChargeRequest)
        );
    }

}
