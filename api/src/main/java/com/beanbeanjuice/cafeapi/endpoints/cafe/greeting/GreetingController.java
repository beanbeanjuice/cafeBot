package com.beanbeanjuice.cafeapi.endpoints.cafe.greeting;

import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/cafe")
public class GreetingController {

    @GetMapping("/")
    public ResponseEntity<?> hello() {
        return ResponseEntity.status(HttpStatus.OK).body(Response.of("Hello, world!"));
    }

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body(Response.of("Hello, public!"));
    }

    @GetMapping("/secured")
    public ResponseEntity<?> secured(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(String.format("Hello %s, you are logged in. Your ID is %d.", principal.getUsername(), principal.getUserId())));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(String.format("You are an admin. Your ID is %d.", principal.getUserId())));
    }

}
