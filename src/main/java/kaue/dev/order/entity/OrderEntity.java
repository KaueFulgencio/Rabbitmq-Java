package kaue.dev.order.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Document(collection = "td_orders")
public class OrderEntity {
    @Id
    private Long orderId;
    private Long clientId;
    private List<Product> products;
    private BigDecimal total;
}
