package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.games;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/cafebot/games")
@RequiredArgsConstructor
@CrossOrigin
public class UserGamesController {

    private final UserGamesService service;

    @GetMapping
    public ResponseEntity<?> getAllGames() {
        return service.getAllGames();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getAllGamesForPlayer(@PathVariable("user_id") int userID, @Nullable @RequestParam("game_type") String gameType) {
        if (gameType == null) return service.getAllGamesForPlayer(userID);
        return service.getAllGamesForPlayerWithType(userID, gameType);
    }

    @GetMapping("/{user_id}/wins")
    public ResponseEntity<?> getWinsForPlayer(@PathVariable("user_id") int userID, @Nullable @RequestParam("game_type") String gameType) {
        if (gameType == null) return service.getAllGamesWonForPlayer(userID);
        return service.getAllGamesWonForPlayerWithType(userID, gameType);
    }

    @GetMapping("/{user_id}/losses")
    public ResponseEntity<?> getLossesForPlayer(@PathVariable("user_id") int userID, @Nullable @RequestParam("game_type") String gameType) {
        if (gameType == null) return service.getAllGamesLostForPlayer(userID);
        return service.getAllGamesLostForPlayerWithType(userID, gameType);
    }

    @GetMapping("/{user_id}/ties")
    public ResponseEntity<?> getTiesForPlayer(@PathVariable("user_id") int userID, @Nullable @RequestParam("game_type") String gameType) {
        if (gameType == null) return service.getAllGamesTiedForPlayer(userID);
        return service.getAllGamesTiedForPlayerWithType(userID, gameType);
    }

    @PutMapping
    public ResponseEntity<?> addGame(@RequestBody CreateGameRequest request) {
        return service.addGame(request);
    }

}
