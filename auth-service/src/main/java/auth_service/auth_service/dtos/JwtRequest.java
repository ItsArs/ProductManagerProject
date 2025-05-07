package auth_service.auth_service.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
