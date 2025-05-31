package notification_service.handlers;


import core.product.ProductCreatedEvent;
import core.product.ProductDeletedEvent;
import core.product.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import notification_service.services.NotificationService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@KafkaListener(topics = {"product-created-events-topic", "product-deleted-events-topic", "product-updated-events-topic"})
@RequiredArgsConstructor
public class ProductEventsHandler {
    private final NotificationService notificationService;

    @KafkaHandler
    public void handleProduct(ProductCreatedEvent product) {
        log.info("Event 1 received: {}", product.toString());
        notificationService.sendCreatedNotification(product);
    }

    @KafkaHandler
    public void handleProduct(ProductUpdatedEvent product) {
        log.info("Event 2 received: {}", product.toString());
        notificationService.sendUpdatedNotification(product);
    }

    @KafkaHandler
    public void handleProduct(ProductDeletedEvent product) {
        log.info("Event 3 received: {}", product.toString());
        notificationService.sendDeletedNotification(product);
    }
}
