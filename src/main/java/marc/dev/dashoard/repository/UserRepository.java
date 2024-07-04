package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity,String> {
    @Query("{}")
    Page<UserEntity> findAllUsers(Pageable pageable);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Page<UserEntity> findUsersByName(String name, Pageable pageable);

    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findUserById(String userId);
}
