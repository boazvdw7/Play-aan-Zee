package app.rest;

import app.exceptions.NotAcceptableException;
import app.models.Users;
import app.repositories.EntityRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.jwt.JWToken;

import java.util.List;
import app.config.APIConfig;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController  {
    private final EntityRepository<Users> userRepository;

    @Autowired
    APIConfig apiConfig;

    public AuthenticationController(EntityRepository<Users> userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<Users> requestLogin(@RequestAttribute("jwToken") @RequestBody ObjectNode loginRequest) {

        List<Users> users = this.userRepository.findAll();
        String userEmail = loginRequest.get("email").asText();
        String userPassword = loginRequest.get("hashedPassword").asText();

        for (Users user : users) {
            if (user.getEmail().equals(userEmail) && user.getHashedPassword().equals(userPassword)) {
                JWToken jwtToken = new JWToken(user.getEmail(), user.getId(), user.getRole());
                String token = jwtToken.encode(this.apiConfig.getIssuer(), this.apiConfig.getPassphrase(), this.apiConfig.getTokenDuration0fValidity());

                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(user);
            }
        }
        throw new NotAcceptableException("Login denied");
    }

    @GetMapping("/users")
    public String getUserInfo(@RequestAttribute(JWToken.JWT_ATTRIBUTE_NAME) JWToken jwToken) {
        String userEmail = jwToken.getCallName();
        Long userId = jwToken.getUserId();
        String userRole = jwToken.getRole();


        return "User Info: Email = " + userEmail + ", ID = " + userId + ", Role = " + userRole + "Token: " + jwToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Users> requestSignUp(@RequestAttribute("jwToken") @RequestBody ObjectNode signUpRequest) {
        List<Users> users = this.userRepository.findAll();

        for(Users user : users) {
            if(user.getEmail().equals(signUpRequest.get("email").asText())) {
                throw new NotAcceptableException("Email already exists");
            }
        }

        String email = signUpRequest.get("email").asText();
        String callname = signUpRequest.get("callname").asText();
        String password = signUpRequest.get("hashedPassword").asText();

        Users user = new Users(email, callname, password);

        userRepository.save(user);

        System.out.println(user);

        if(users.contains(signUpRequest)) {
            throw new NotAcceptableException("User already exists");
        }

        JWToken jwtToken = new JWToken(user.getEmail(), user.getId(), user.getRole());
        String token = jwtToken.encode(this.apiConfig.getIssuer(), this.apiConfig.getPassphrase(), this.apiConfig.getTokenDuration0fValidity());



        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(user);
    }
}