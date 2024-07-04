package marc.dev.dashoard.service.impl;

import marc.dev.dashoard.entity.ProductEntity;
import marc.dev.dashoard.entity.ProductStatEntity;
import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.repository.ProductRepository;
import marc.dev.dashoard.repository.ProductStatRepository;
import marc.dev.dashoard.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductStatRepository productStatRepository;
    @Override
    public Page<ProductEntity> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllProducts(pageable);
    }

    @Override
    public List<ProductEntity> getProductsWithStats(int page, int size){
        Pageable pageable = PageRequest.of( page, size);
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        List<ProductEntity> products = productPage.getContent();

        return products.stream().map(product->{
            List<ProductStatEntity> stats = productStatRepository.findByProductId(product.getId()).orElseThrow(()-> new RuntimeException("Product not found"));
            product.setProductStats(stats);
            return product;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<ProductEntity> getProducts(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductsByName(name, pageable);
    }

    @Override
    public ProductEntity getProduct(String id) {
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
    }
}
