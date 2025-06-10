package core.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductUpdatedEvent {
    private String productName;
    private double productPrice;
    private double lastProductPrice;
}
