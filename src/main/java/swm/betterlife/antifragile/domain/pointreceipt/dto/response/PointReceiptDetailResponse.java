package swm.betterlife.antifragile.domain.pointreceipt.dto.response;

import static swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceiptType.CHARGE;

import lombok.Builder;
import swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceipt;
import swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceiptType;

@Builder
public record PointReceiptDetailResponse(
    Integer amount,
    PointReceiptType type,
    PurchaseItemResponse purchaseItem
) {
    public static PointReceiptDetailResponse from(PointReceipt pointReceipt) {
        if (pointReceipt.getType().equals(CHARGE)) {
            return PointReceiptDetailResponse.builder()
                .amount(pointReceipt.getAmount())
                .type(pointReceipt.getType())
                .build();
        } else {
            return PointReceiptDetailResponse.builder()
                .amount(pointReceipt.getAmount())
                .type(pointReceipt.getType())
                .purchaseItem(PurchaseItemResponse.from(pointReceipt.getPurchaseItem()))
                .build();
        }
    }
}
