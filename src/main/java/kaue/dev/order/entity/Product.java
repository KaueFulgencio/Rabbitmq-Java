package kaue.dev.order.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "tb_product")
public class Product {

    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
