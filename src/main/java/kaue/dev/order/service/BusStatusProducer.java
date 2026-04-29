package kaue.dev.order.service;

import kaue.dev.order.config.RabbitMqConfig;
import kaue.dev.order.listener.dto.BusStatusCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusStatusProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendBusStatusCreatedEvent(BusStatusCreatedEvent event) {
        System.out.println("Enviando evento para RabbitMQ: " + event);
        rabbitTemplate.convertAndSend(RabbitMqConfig.BUS_STATUS_EXCHANGE, RabbitMqConfig.BUS_STATUS_ROUTING_KEY, event);
        System.out.println("Evento enviado com sucesso.");
    }
}
