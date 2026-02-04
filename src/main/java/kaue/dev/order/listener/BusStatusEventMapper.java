package kaue.dev.order.listener;

import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.listener.dto.BusStatusCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class BusStatusEventMapper {

    public BusStatusEntity toEntity(BusStatusCreatedEvent event) {
        return new BusStatusEntity(
                event.getLineName(),
                event.getLineNumber(),
                event.isActive(),
                event.getLatitude(),
                event.getLongitude(),
                event.getTimestamp()
        );
    }
}
