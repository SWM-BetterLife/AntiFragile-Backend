package swm.betterlife.antifragile.domain.pointReceipt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointReceiptType {
    PURCHASE("사용"), CHARGE("충전");

    private final String toKorean;
}
