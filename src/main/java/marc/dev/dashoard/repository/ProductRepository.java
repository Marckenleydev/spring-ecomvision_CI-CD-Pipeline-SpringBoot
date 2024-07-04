package marc.dev.dashoard.repository;

import marc.dev.dashoard.entity.ProductEntity;
import marc.dev.dashoard.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {

    @Query("{}")
    Page<ProductEntity> findAllProducts(Pageable pageable);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Page<ProductEntity> findProductsByName(String name, Pageable pageable);

}


