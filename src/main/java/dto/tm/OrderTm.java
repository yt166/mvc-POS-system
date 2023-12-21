package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OrderTm extends RecursiveTreeObject<OrderTm> {
    private String code;
    private String description;
    private int qty;
    private Double prize;
    private JFXButton btn;
}
