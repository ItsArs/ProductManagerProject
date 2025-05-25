package auth_service.controllers;


import auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/users")
    public String adminData() {
        return userService.findAll().toString();
    }


    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
