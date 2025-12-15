package kaue.dev.order.listener.dto;

import kaue.dev.order.entity.Product;
import java.util.*;

public record OrderCreatedEvent(Long orderId, Long clientId, List<OrderProductEvent> products) {

}
