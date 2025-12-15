package kaue.dev.order.service;

import kaue.dev.order.entity.OrderEntity;
import kaue.dev.order.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public void save(OrderEntity order) {
        orderRepository.save(order);
    }

}
