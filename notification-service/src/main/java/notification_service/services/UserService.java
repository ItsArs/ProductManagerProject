package notification_service.services;

import core.CreatedUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import notification_service.dtos.UserDTO;
import notification_service.entities.User;
import notification_service.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
    }

    @Transactional
    public void saveUser(CreatedUserEvent createdUserEvent) {
        userRepository.save(modelMapper.map(createdUserEvent, User.class));
    }

    @Transactional
    public ResponseEntity<?> updateUser(UserDTO userDTO, Principal principal) {
        if (!userDTO.getUsername().equals(principal.getName())) {
            log.info("Username not match");
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

        if (user == null) {
            log.info("Username not found");
            return ResponseEntity.notFound().build();
        }

        modelMapper.map(userDTO, user);

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
