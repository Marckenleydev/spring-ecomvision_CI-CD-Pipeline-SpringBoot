package marc.dev.dashoard.service;


import marc.dev.dashoard.entity.ProductEntity;
import marc.dev.dashoard.entity.ProductStatEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductStatService {
    Page<ProductStatEntity> getProductStats(int page, int size);
    Page<ProductStatEntity> getProductStats(int page, int size, String name);
    ProductStatEntity getProductStat(String id);


}
