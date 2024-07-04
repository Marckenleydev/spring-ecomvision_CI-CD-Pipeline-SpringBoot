package marc.dev.dashoard.service;

import marc.dev.dashoard.entity.ProductEntity;
import marc.dev.dashoard.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductEntity> getProducts(int page, int size);
    Page<ProductEntity> getProducts(int page, int size, String name);
    ProductEntity getProduct(String id);
    List<ProductEntity> getProductsWithStats(int page, int size);
}
