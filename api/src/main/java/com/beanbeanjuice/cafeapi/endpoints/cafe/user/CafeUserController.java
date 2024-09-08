package com.beanbeanjuice.cafeapi.endpoints.cafe.user;

import com.beanbeanjuice.cafeapi.endpoints.cafe.authentication.CreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafe/user")
@RequiredArgsConstructor
@CrossOrigin
public class CafeUserController {

    private final CafeUserService userService;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated CreateRequest request) {
        return userService.createUser(request, passwordEncoder);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

}
