package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TransactionRepository extends MongoRepository<TransactionEntity, String> {
    @Query("{'$or': [{'userId': {$regex: ?0, $options: 'i'}}, {'cost': {$regex: ?0, $options: 'i'}}]}")
    Page<TransactionEntity> findBySearchTerm(String searchTerm, Pageable pageable);

    List<TransactionEntity> findTop50ByOrderByCreatedAtDesc();
}
