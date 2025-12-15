package kaue.dev.order.listener.dto;

import java.math.BigDecimal;

public record OrderProductEvent(String productName, Integer quantity, BigDecimal price) {
}
