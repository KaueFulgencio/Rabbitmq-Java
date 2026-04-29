package kaue.dev.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String BUS_STATUS_EXCHANGE = "bus-status-exchange";
    public static final String BUS_STATUS_QUEUE = "bus-status-queue";
    public static final String BUS_STATUS_ROUTING_KEY = "bus.status.created";

    // DLQ constants
    public static final String BUS_STATUS_DLX = "bus-status-dlx";
    public static final String BUS_STATUS_DLQ = "bus-status-dlq";
    public static final String BUS_STATUS_DLQ_ROUTING_KEY = "bus.status.failed";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange busStatusExchange() {
        return new DirectExchange(BUS_STATUS_EXCHANGE, true, false);
    }

    @Bean
    public Queue busStatusQueue() {
        return QueueBuilder
                .durable(BUS_STATUS_QUEUE)
                .withArgument("x-dead-letter-exchange", BUS_STATUS_DLX)
                .withArgument("x-dead-letter-routing-key", BUS_STATUS_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding busStatusBinding() {
        return BindingBuilder
                .bind(busStatusQueue())
                .to(busStatusExchange())
                .with(BUS_STATUS_ROUTING_KEY);
    }

    // DLQ Exchange
    @Bean
    public DirectExchange busStatusDlx() {
        return new DirectExchange(BUS_STATUS_DLX, true, false);
    }

    // DLQ Queue
    @Bean
    public Queue busStatusDlq() {
        return QueueBuilder
                .durable(BUS_STATUS_DLQ)
                .build();
    }

    // DLQ Binding
    @Bean
    public Binding busStatusDlqBinding() {
        return BindingBuilder
                .bind(busStatusDlq())
                .to(busStatusDlx())
                .with(BUS_STATUS_DLQ_ROUTING_KEY);
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
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // Manual ACK
        return factory;
    }
}