package notification_service.controllers;

import lombok.RequiredArgsConstructor;
import notification_service.dtos.UserDTO;
import notification_service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PatchMapping("/notification-set")
    public ResponseEntity<?> setNotification(@RequestBody UserDTO userDTO, Principal principal) {
        return userService.updateUser(userDTO, principal);
    }

    @GetMapping("/notification-check")
    public ResponseEntity<?> checkNotification(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }
}
