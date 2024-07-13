package swm.betterlife.antifragile.domain.pointreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    private ItemType itemType;

    private String itemId;

}
