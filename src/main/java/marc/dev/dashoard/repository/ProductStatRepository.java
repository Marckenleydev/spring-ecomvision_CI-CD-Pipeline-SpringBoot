package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.ProductStatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductStatRepository extends MongoRepository<ProductStatEntity, String> {
    Optional<List<ProductStatEntity>> findByProductId(String productId);
}
