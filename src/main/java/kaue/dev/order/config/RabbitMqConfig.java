package kaue.dev.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_CREATED_QUEUE = "api_queue-created";
    public static final String BUS_STATUS_EXCHANGE = "bus-status-exchange";
    public static final String BUS_STATUS_QUEUE = "bus-status-queue";
    public static final String BUS_STATUS_ROUTING_KEY = "bus.status.created";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarable orderCreatedQueue(){
        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public DirectExchange busStatusExchange() {
        return new DirectExchange(BUS_STATUS_EXCHANGE, true, false);
    }

    @Bean
    public Queue busStatusQueue() {
        return QueueBuilder
                .durable(BUS_STATUS_QUEUE)
                .build();
    }

    @Bean
    public Binding busStatusBinding() {
        return BindingBuilder
                .bind(busStatusQueue())
                .to(busStatusExchange())
                .with(BUS_STATUS_ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
}