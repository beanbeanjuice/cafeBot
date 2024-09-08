package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/code")
@RequiredArgsConstructor
@CrossOrigin
public class CodeController {

    private final UserCodeService codeService;

    @PostMapping("/{user_id}")
    public ResponseEntity<?> generateCode(@PathVariable("user_id") int userID) {
        return codeService.generateCode(userID);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getCode(@PathVariable("user_id") int userID) {
        return codeService.getCode(userID);
    }

}
