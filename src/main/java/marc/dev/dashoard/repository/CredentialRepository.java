package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.CredentialEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CredentialRepository extends MongoRepository<CredentialEntity , String> {
    Optional<CredentialEntity> getCredentialByUserEntityId(String userId);
}
