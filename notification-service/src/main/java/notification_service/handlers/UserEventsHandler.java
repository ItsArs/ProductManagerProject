package notification_service.handlers;


import core.CreatedUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import notification_service.services.UserService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@KafkaListener(topics = {"user-created-events-topic"})
@RequiredArgsConstructor
public class UserEventsHandler {
    private final UserService userService;

    @KafkaHandler
    public void HandleUser(CreatedUserEvent event) {
        log.info("Received user created event: {}", event);
        userService.saveUser(event);
        log.info("User has been saved: {}", event.getUsername());
    }
}
