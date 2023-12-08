package com.hekshot.skillrebe.Controller;

import com.hekshot.skillrebe.Dto.UserDto;
import com.hekshot.skillrebe.Entity.Login;
import com.hekshot.skillrebe.Entity.User;
import com.hekshot.skillrebe.Exception.UsernameAlreadyExistsException;
import com.hekshot.skillrebe.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserRepo usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/user")
    public ResponseEntity<?> userDetails(@AuthenticationPrincipal OAuth2User oAuth2User) {
        try {
            String userEmail = oAuth2User.getAttribute("email");

            if (userEmail != null) {
                User user = usersRepository.findByUserName(userEmail);

                if (user != null) {
                    UserDto userDto = new UserDto(user);
                    return ResponseEntity.ok(userDto);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email attribute not present");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) throws UsernameAlreadyExistsException {
        User existingUser = usersRepository.findByUserName(user.getUserName());
        System.out.println(user.getUserPassword());
        if (existingUser != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        } else {
            if (user.getUserPassword() != null) {
                user.setUserName(user.getUserName());
                user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
                usersRepository.save(user);
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.badRequest().body("Password cannot be null");
            }
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Login login) {

        User user = usersRepository.findByUserName(login.getUserName());

        if (user != null && passwordEncoder.matches(login.getUserPassword(), user.getUserPassword()) ) {
            UserDto userDto = new UserDto(user);
            return ResponseEntity.ok(userDto);
        }
        else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed");
        }
    }


    @GetMapping("/logout")
    public String logout() {
        // Invalidate the current user's session
        SecurityContextHolder.getContext().setAuthentication(null);

        return "Logout successful";
    }


}
