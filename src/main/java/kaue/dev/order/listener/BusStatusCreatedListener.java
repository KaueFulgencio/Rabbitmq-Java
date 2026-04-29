package kaue.dev.order.listener;

import com.rabbitmq.client.Channel;
import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.listener.dto.BusStatusCreatedEvent;
import kaue.dev.order.service.BusStatusService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BusStatusCreatedListener {

    @Autowired
    private BusStatusService busStatusService;

    @Autowired
    private BusStatusEventMapper mapper;

    // Flag para simular erro (pode ser alterado via endpoint)
    public static boolean SIMULATE_ERROR = false;

    @RabbitListener(queues = "bus-status-queue")
    public void onBusStatusCreated(BusStatusCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            System.out.println("📨 Recebendo evento: " + event);

            // Simular erro se flag estiver ativa
            if (SIMULATE_ERROR) {
                System.out.println("❌ Simulando erro no processamento...");
                throw new RuntimeException("Erro simulado para demonstração de DLQ");
            }

            BusStatusEntity entity = mapper.toEntity(event);
            busStatusService.save(entity);

            // ACK manual: confirma o processamento
            channel.basicAck(deliveryTag, false);
            System.out.println("✅ Evento processado com sucesso e ACK enviado.");

        } catch (Exception e) {
            System.err.println("⚠️ Erro ao processar evento: " + e.getMessage());
            // NACK: rejeita a mensagem sem requeue, enviando para DLQ
            channel.basicNack(deliveryTag, false, false);
            System.out.println("📤 Mensagem rejeitada e enviada para DLQ.");
        }
    }
}
