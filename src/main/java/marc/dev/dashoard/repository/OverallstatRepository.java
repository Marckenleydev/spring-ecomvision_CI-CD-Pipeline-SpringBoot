package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.OverallStatEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OverallstatRepository extends MongoRepository<OverallStatEntity, String> {
    @Aggregation(pipeline = {"{$project: {monthlyData:1, _id:0}}"})
    List<OverallStatEntity> findAllMonthlyData();
    List<OverallStatEntity> findByYear(int year);
}
