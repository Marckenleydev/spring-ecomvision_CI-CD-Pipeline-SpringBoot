package marc.dev.dashoard.service.impl;

import marc.dev.dashoard.entity.ProductStatEntity;
import marc.dev.dashoard.service.ProductStatService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductStatServiceImpl implements ProductStatService {

    @Override
    public Page<ProductStatEntity> getProductStats(int page, int size) {
        return null;
    }

    @Override
    public Page<ProductStatEntity> getProductStats(int page, int size, String name) {
        return null;
    }

    @Override
    public ProductStatEntity getProductStat(String id) {
        return null;
    }
}
