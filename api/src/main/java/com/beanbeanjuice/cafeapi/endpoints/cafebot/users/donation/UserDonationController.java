package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/donations")
@RequiredArgsConstructor
@CrossOrigin
public class UserDonationController {

    private final UserDonationService service;

    @GetMapping
    public ResponseEntity<?> getAllDonations() {
        return service.getAllDonations();
    }

    @PutMapping
    public ResponseEntity<?> addDonation(@RequestParam("sender_id") int senderID, @RequestParam("receiver_id") int receiverID) {
        return service.addDonation(senderID, receiverID);
    }

    @GetMapping("/sent/{user_id}")
    public ResponseEntity<?> getDonationsSent(@PathVariable("user_id") int userID) {
        return service.getDonationsSent(userID);
    }

    @GetMapping("/received/{user_id}")
    public ResponseEntity<?> getDonationsReceived(@PathVariable("user_id") int userID) {
        return service.getDonationsReceived(userID);
    }

}
