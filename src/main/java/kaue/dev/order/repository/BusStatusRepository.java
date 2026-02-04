package kaue.dev.order.repository;

import kaue.dev.order.entity.BusStatusEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStatusRepository extends MongoRepository<BusStatusEntity, String> {
}

