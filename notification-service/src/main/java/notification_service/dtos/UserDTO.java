package notification_service.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private boolean createdNotifications;
    private boolean updatedNotifications;
    private boolean deletedNotifications;
}
