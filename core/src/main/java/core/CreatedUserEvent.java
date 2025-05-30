package core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@AllArgsConstructor
@ToString
public class CreatedUserEvent {
    private String username;
    private Collection<String> roles;
}
