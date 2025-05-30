package core.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ProductUpdatedEvent {
    private String productName;
    private double productPrice;
    private double lastProductPrice;
}
