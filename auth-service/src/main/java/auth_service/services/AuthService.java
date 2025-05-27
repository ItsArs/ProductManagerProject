package auth_service.services;


import auth_service.dtos.JwtRequest;
import auth_service.dtos.JwtResponse;
import auth_service.dtos.RegistrationUserDto;
import auth_service.entities.Role;
import auth_service.entities.User;
import auth_service.exceptions.AppError;
import auth_service.utils.JwtTokenUtils;
import core.CreatedUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final KafkaTemplate<String, CreatedUserEvent> kafkaTemplate;


    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails, userService.findByUsername(authRequest.getUsername()).get().getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }


    public ResponseEntity<?> registration(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword()))  {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Password not confirmed"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent())  {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Username already used"), HttpStatus.BAD_REQUEST);
        }


        userService.createNewUser(registrationUserDto);
        User user = userService.findByUsername(registrationUserDto.getUsername()).get();
        CreatedUserEvent createdUserEvent = new CreatedUserEvent(user.getUsername(), user.getRoles().stream().map(Role::getName).toList());

        CompletableFuture<SendResult<String, CreatedUserEvent>> future = kafkaTemplate.send("user-created-events-topic", "user", createdUserEvent);

        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Got some troubles with processing CreatedUserEvent: {}", throwable.getMessage());
            } else{
                log.info("CreatedUserEvent done successfully: {}", result.getRecordMetadata());
            }
        });

        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
