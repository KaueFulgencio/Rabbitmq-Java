package kaue.dev.order.listener;

import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.listener.dto.BusStatusCreatedEvent;
import kaue.dev.order.service.BusStatusService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusStatusCreatedListener {

    @Autowired
    private BusStatusService busStatusService;

    @Autowired
    private BusStatusEventMapper mapper;

    @RabbitListener(queues = "bus-status-queue")
    public void onBusStatusCreated(BusStatusCreatedEvent event) {
        BusStatusEntity entity = mapper.toEntity(event);
        busStatusService.save(entity);
    }
}

