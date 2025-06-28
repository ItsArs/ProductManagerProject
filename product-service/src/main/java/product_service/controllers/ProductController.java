package product_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product_service.dtos.ProductDto;
import product_service.entities.Product;
import product_service.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> allProducts() {
        return productService.findAll();
    }


    @GetMapping("/{id}")
    public Product getProduct(@PathVariable int id) {
        return productService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto product) {
        return productService.createNewProduct(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable int id, @RequestBody double newPrice) {
        return productService.updatePrice(id, newPrice);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }
}
