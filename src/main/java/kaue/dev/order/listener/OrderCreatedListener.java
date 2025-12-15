package kaue.dev.order.listener;

import kaue.dev.order.entity.OrderEntity;
import kaue.dev.order.listener.dto.OrderCreatedEvent;
import kaue.dev.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

import static kaue.dev.order.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {

    private final Logger  logger = LoggerFactory.getLogger(OrderCreatedListener.class);
    private final OrderRepository orderRepository;

    public OrderCreatedListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        OrderEntity document = OrderEventMapper.toDocument(message.getPayload());
        orderRepository.save(document);

        logger.info("message: {}", message);
    }
}
