package notification_service.services;

import core.CreatedUserEvent;
import lombok.RequiredArgsConstructor;
import notification_service.dtos.UserDTO;
import notification_service.entities.User;
import notification_service.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Transactional
    public void saveUser(CreatedUserEvent createdUserEvent) {
        User user = new User();
        user.setUsername(createdUserEvent.getUsername());
        user.setEmail(createdUserEvent.getEmail());

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());

        user.setUpdatedNotifications(userDTO.isUpdatedNotifications());
        user.setDeletedNotifications(userDTO.isDeletedNotifications());
        user.setCreatedNotifications(userDTO.isCreatedNotifications());

        userRepository.save(user);
    }

}
