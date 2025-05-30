package product_service.services;

import core.CreatedUserEvent;
import core.product.ProductCreatedEvent;
import core.product.ProductDeletedEvent;
import core.product.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product_service.dtos.ProductDto;
import product_service.entities.Product;
import product_service.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService{
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplateCreatedProduct;
    private final KafkaTemplate<String, ProductUpdatedEvent> kafkaTemplateUpdatedProduct;
    private final KafkaTemplate<String, ProductDeletedEvent> kafkaTemplateDeletedProduct;


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

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(product.getProductName(), product.getProductPrice());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplateCreatedProduct.send("product-created-events-topic", "product", productCreatedEvent);

        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Got some troubles with processing ProductCreatedEvent: {}", throwable.getMessage());
            } else{
                log.info("ProductCreatedEvent done successfully: {}", result.getRecordMetadata());
            }
        });


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
        double oldPrice = product.getProductPrice();
        product.setProductPrice(newPrice);
        productRepository.save(product);

        ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(product.getProductName(), product.getProductPrice(), oldPrice);

        CompletableFuture<SendResult<String, ProductUpdatedEvent>> future = kafkaTemplateUpdatedProduct.send("product-updated-events-topic", "product", productUpdatedEvent);

        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Got some troubles with processing ProductUpdatedEvent: {}", throwable.getMessage());
            } else{
                log.info("ProductUpdatedEvent done successfully: {}", result.getRecordMetadata());
            }
        });


        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteProduct(int id) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            productRepository.deleteById(id);

            ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(product.getProductName());

            CompletableFuture<SendResult<String, ProductDeletedEvent>> future = kafkaTemplateDeletedProduct.send("product-deleted-events-topic", "product", productDeletedEvent);

            future.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("Got some troubles with processing ProductDeletedEvent: {}", throwable.getMessage());
                } else{
                    log.info("ProductDeletedEvent done successfully: {}", result.getRecordMetadata());
                }
            });

            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }
}
