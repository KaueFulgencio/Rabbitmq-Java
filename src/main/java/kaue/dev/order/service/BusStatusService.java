package kaue.dev.order.service;

import kaue.dev.order.entity.BusStatusEntity;
import kaue.dev.order.repository.BusStatusRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusStatusService {

    @Autowired
    private BusStatusRepository busStatusRepository;

    public List<BusStatusEntity> findAll() {
        return busStatusRepository.findAll();
    }

    public BusStatusEntity save(BusStatusEntity busStatus) {
        return busStatusRepository.save(busStatus);
    }

    @RabbitListener(queues = "bus-status-queue")
    public void consumeBusStatus(BusStatusEntity busStatus) {
        busStatusRepository.save(busStatus);
    }
}
