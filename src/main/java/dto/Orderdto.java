package dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Orderdto {
    private String orderID;
    private String date;
    private String cusID;
    private List<OrderDetailsdto>list;
}
