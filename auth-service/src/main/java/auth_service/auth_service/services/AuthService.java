package auth_service.auth_service.services;


import auth_service.auth_service.dtos.JwtRequest;
import auth_service.auth_service.dtos.JwtResponse;
import auth_service.auth_service.dtos.RegistrationUserDto;
import auth_service.auth_service.exceptions.AppError;
import auth_service.auth_service.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;


    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
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
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
