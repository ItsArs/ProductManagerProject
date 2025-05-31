package core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@AllArgsConstructor
@ToString
public class UpdatedUserEvent {
    private String username;
    private String email;
    private boolean createdNotifications;
    private boolean updatedNotifications;
    private boolean deletedNotifications;
    private Collection<String> roles;
}
