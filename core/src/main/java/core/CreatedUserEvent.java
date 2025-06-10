package core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatedUserEvent {
    private String username;
    private String email;
    private Collection<String> roles;
}
