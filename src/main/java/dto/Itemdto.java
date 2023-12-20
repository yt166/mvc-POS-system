package dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Itemdto {
    private String itemCode;
    private String description;
    private Double unitPrize;
    private int qtyOnHand;
}
