package dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsdto {
    private String orderID;
    private String itemCode;
    private int qty;
    private double unitPrize;
}