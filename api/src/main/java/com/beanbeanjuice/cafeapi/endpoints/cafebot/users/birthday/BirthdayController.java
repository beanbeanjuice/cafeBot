package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.birthday;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/birthday")
@RequiredArgsConstructor
@CrossOrigin
public class BirthdayController {

    private final UserBirthdayService service;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getBirthday(@PathVariable("user_id") int userID) {
        return this.service.getBirthday(userID);
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<?> setBirthday(@PathVariable("user_id") int userID, @RequestBody CreateBirthdayRequest request) {
        return this.service.updateBirthday(userID, request);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteBirthday(@PathVariable("user_id") int userID) {
        return this.service.deleteBirthday(userID);
    }

}
