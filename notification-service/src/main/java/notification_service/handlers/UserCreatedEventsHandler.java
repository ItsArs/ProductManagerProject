package notification_service.handlers;


import core.CreatedUserEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@KafkaListener(topics = "user-created-events-topic")
public class UserCreatedEventsHandler {

    @KafkaHandler
    public void HandleUser(CreatedUserEvent event) {
        log.info("Received user created event: {}", event);
    }
}
