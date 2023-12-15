package dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customertm {
    private String cusid;
    private String cusName;
    private String cusAddress;
    private double cusSalary;
    private Button btn;
}
