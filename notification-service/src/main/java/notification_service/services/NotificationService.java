package notification_service.services;


import core.product.ProductCreatedEvent;
import core.product.ProductDeletedEvent;
import core.product.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;

    public void sendCreatedNotification(ProductCreatedEvent productCreatedEvent) {
        userService.getAllUsers().forEach(user -> {
            if (user.isCreatedNotifications()) {
                log.info("CreatedNotification to {}, with {}", user, productCreatedEvent);
            }
        });
    }

    public void sendUpdatedNotification(ProductUpdatedEvent productUpdatedEvent) {
        userService.getAllUsers().forEach(user -> {
            if (user.isUpdatedNotifications()) {
                log.info("UpdatedNotification to {}, with {}", user, productUpdatedEvent);
            }
        });
    }

    public void sendDeletedNotification(ProductDeletedEvent productDeletedEvent) {
        userService.getAllUsers().forEach(user -> {
            if (user.isDeletedNotifications()) {
                log.info("DeletedNotification to {}, with {}", user, productDeletedEvent);
            }
        });
    }
}
