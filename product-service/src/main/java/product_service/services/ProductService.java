package product_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product_service.dtos.ProductDto;
import product_service.entities.Product;
import product_service.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int id) {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Transactional
    public HttpStatus createNewProduct(ProductDto productDto) {
        Product product = new Product();

        product.setProductName(productDto.getProductName());
        product.setProductPrice(productDto.getProductPrice());
        product.setCreatedAt(LocalDateTime.now());

        productRepository.save(product);

        return HttpStatus.CREATED;
    }

    @Transactional
    public HttpStatus updatePrice(int id, double newPrice) {
        Product product = new Product();
        if (productRepository.findById(id).isPresent()) {
            product = productRepository.findById(id).get();
        } else {
            return HttpStatus.NOT_FOUND;
        }
        product.setProductPrice(newPrice);
        productRepository.save(product);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteProduct(int id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }
}
