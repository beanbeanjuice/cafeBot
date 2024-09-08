package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafe/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        return service.attemptLogin(request.getUsername(), request.getPassword());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "_auth_refresh") String refreshToken) {
        return service.refreshAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "_auth_refresh") String refreshToken) {
        return service.revokeRefreshToken(refreshToken);
    }

}
