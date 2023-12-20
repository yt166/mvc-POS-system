package dto.tm;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemTm extends RecursiveTreeObject<ItemTm> {
    private String code;
    private String description;
    private Double unitPrize;
    private int qtyOnHand;
    private Button btnDelete;
}
