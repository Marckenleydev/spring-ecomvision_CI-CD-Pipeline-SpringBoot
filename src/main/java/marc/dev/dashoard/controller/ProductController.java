package marc.dev.dashoard.controller;

import marc.dev.dashoard.entity.ProductEntity;
import marc.dev.dashoard.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

//    @GetMapping
//    public Page<ProductEntity> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size) {
//        return productService.getProducts(page, size);
//    }

    @GetMapping
    public List<ProductEntity> getProductsWithStats(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "20") int size) {
        return productService.getProductsWithStats(page, size);
    }

    @GetMapping("/search")
    public Page<ProductEntity> getProductsByName(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size , @RequestParam String name) {
        return productService.getProducts(page, size, name);
    }
    @GetMapping("{productId}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable("productId") String productId){
        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
    }


}
