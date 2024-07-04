package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.ConfirmationEntity;
import marc.dev.dashoard.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfirmationRepository extends MongoRepository<ConfirmationEntity, String> {
    Optional<ConfirmationEntity> findByToken(String token);
    Optional<ConfirmationEntity> findByUserEntity(UserEntity userEntity);
}
