package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.interactions;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/interactions")
@RequiredArgsConstructor
@CrossOrigin
public class UserInteractionController {

    private final UserInteractionsService service;

    @GetMapping
    public ResponseEntity<?> getAllInteractions(@Nullable @RequestParam("user_id") Integer userID) {
        if (userID == null) return service.getAllInteractions();
        return service.getAllInteractionsForUser(userID);
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllInteractionsForType(@PathVariable("type") String type, @Nullable @RequestParam("user_id") Integer userID) {
        if (userID == null) return service.getAllInteractionsByType(type);
        return service.getAllInteractionsByTypeForUser(userID, type);
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> createInteraction(@PathVariable("type") String type, @RequestParam("sender_id") int senderID, @RequestParam("receiver_id") int receiverID) {
        return service.createInteraction(senderID, receiverID, type);
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getInteractionsSent(@RequestParam("user_id") int userID) {
        return service.getAllInteractionsSentForUser(userID);
    }

    @GetMapping("/sent/{type}")
    public ResponseEntity<?> getAllInteractionsSentByTypeForUser(@PathVariable("type") String type, @RequestParam("user_id") int userID) {
        return service.getAllInteractionsSentByTypeForUser(userID, type);
    }

    @GetMapping("/received")
    public ResponseEntity<?> getInteractionsReceived(@RequestParam("user_id") int userID) {
        return service.getAllInteractionsReceivedForUser(userID);
    }

    @GetMapping("/received/{type}")
    public ResponseEntity<?> getAllInteractionsReceivedByTypeForUser(@PathVariable("type") String type, @RequestParam("user_id") int userID) {
        return service.getAllInteractionsReceivedByTypeForUser(userID, type);
    }

}
