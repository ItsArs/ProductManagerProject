package core;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class CreatedUserEvent {
    private String username;
    private Collection<String> roles;
}
