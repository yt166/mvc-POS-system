package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Customerdto {
    private String cusID;
    private String cusName;
    private String cusAddress;
    private double cusSalary;

}
