package swm.betterlife.antifragile.domain.pointreceipt.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.pointreceipt.entity.Item;
import swm.betterlife.antifragile.domain.pointreceipt.entity.ItemType;

@Builder
public record PurchaseItemResponse(
    ItemType itemType,
    String itemId
) {
    public static PurchaseItemResponse from(Item item) {
        return PurchaseItemResponse.builder()
            .itemType(item.getItemType())
            .itemId(item.getItemId())
            .build();
    }
}
