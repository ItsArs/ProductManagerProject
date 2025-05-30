package core.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ProductCreatedEvent {
    private String productName;
    private double productPrice;
}
