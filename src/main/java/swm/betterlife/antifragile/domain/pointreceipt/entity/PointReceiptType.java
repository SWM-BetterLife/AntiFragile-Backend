package swm.betterlife.antifragile.domain.pointreceipt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointReceiptType {
    PURCHASE("사용"), CHARGE("충전");

    private final String toKorean;
}
