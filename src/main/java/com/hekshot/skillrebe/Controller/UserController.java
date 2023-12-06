package com.hekshot.skillrebe.Controller;

import com.hekshot.skillrebe.Entity.Login;
import com.hekshot.skillrebe.Entity.User;
import com.hekshot.skillrebe.Exception.UsernameAlreadyExistsException;
import com.hekshot.skillrebe.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepo usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @GetMapping("/user")
    public ResponseEntity<?> userDetails(@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = usersRepository.findByUserName(oAuth2User.getAttribute("email"));
        return ResponseEntity.ok(user);
    }


    @PostMapping("/register")
    public ResponseEntity<?> addUser( @RequestBody User user ) throws UsernameAlreadyExistsException {

        User existingUser = usersRepository.findByUserName(user.getUserName());
        if (existingUser != null) {

            throw new UsernameAlreadyExistsException("Username already exists");

        }else{

            user.setUserName(user.getUserName());
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            usersRepository.save(user);
            return ResponseEntity.ok("success");

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Login login) {

        User user = usersRepository.findByUserName(login.getUserName());

        if (user != null && passwordEncoder.matches(login.getUserPassword(), user.getUserPassword()) ) {

            return ResponseEntity.ok(user);
        }
        else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed");
        }
    }


    @GetMapping("/logout")
    public String logout() {
        // Invalidate the current user's session
        SecurityContextHolder.getContext().setAuthentication(null);

        // Revoke the OAuth2 access token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);

            if (client != null) {
                authorizedClientService.removeAuthorizedClient(clientRegistrationId, principalName);
            }
        }

        return "Logout successful";
    }


    @PostMapping("/post")
    public ResponseEntity<?> userPost(@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = usersRepository.findByUserName(oAuth2User.getAttribute("email"));
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/put")
    public ResponseEntity<?> userPut(@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = usersRepository.findByUserName(oAuth2User.getAttribute("email"));
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> userDelete(@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = usersRepository.findByUserName(oAuth2User.getAttribute("email"));
        return ResponseEntity.ok(user);
    }
}
