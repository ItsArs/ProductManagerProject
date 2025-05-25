package profile_service.events_handler;

import core.CreatedUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "user-created-events-topic")
public class UserCreatedEventHandler {

    @KafkaHandler
    public void handle(CreatedUserEvent event) {
        log.info("User created event received: {}", event);
    }
}
