package auth_service.services;


import auth_service.entities.Role;
import auth_service.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }
}
