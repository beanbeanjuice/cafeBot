package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable("user_id") int userID) {
        return this.userService.getUser(userID);
    }

    @PutMapping("/{snowflake_id}")
    public ResponseEntity<?> addUser(@PathVariable("snowflake_id") long snowflakeID) {
        return this.userService.addUserFromSnowflake(snowflakeID);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") int userID) {
        return this.userService.deleteUser(userID);
    }

}
