package dto.tm;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.Button;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customertm extends RecursiveTreeObject<Customertm> {
    private String cusid;
    private String cusName;
    private String cusAddress;
    private double cusSalary;
    private Button btn;
}
