package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.AffiliateStatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AffiliateStatRepository extends MongoRepository<AffiliateStatEntity, String> {
}
