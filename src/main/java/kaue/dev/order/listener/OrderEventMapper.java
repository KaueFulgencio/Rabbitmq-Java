package kaue.dev.order.listener;

import kaue.dev.order.entity.OrderEntity;
import kaue.dev.order.entity.Product;
import kaue.dev.order.listener.dto.OrderCreatedEvent;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class OrderEventMapper {
    public static OrderEntity toDocument(OrderCreatedEvent orderCreatedEvent){
        OrderEntity document = new OrderEntity();
        document.setOrderId(orderCreatedEvent.orderId());
        document.setClientId(orderCreatedEvent.clientId());

        var products = orderCreatedEvent.products().stream().map(p -> {
            Product product = new Product();
            product.setProductName(p.productName());
            product.setQuantity(p.quantity());
            product.setPrice(p.price());
            return product;
        }).toList();

        document.setProducts(products);

        BigDecimal total = products.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        document.setTotal(total);

        return document;
    }
}
