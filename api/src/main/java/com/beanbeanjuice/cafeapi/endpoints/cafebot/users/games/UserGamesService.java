package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.games;

import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGamesService {

    @Transactional
    public ResponseEntity<?> getAllGames() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game", UserGamesEntity.class).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved all games.").body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesForPlayer(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.opponent1ID = :user_id OR game.opponent2ID = :user_id", UserGamesEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved all games for the player.").body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games for player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesForPlayerWithType(int userID, String gameType) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.gameType = :game_type AND (game.opponent1ID = :user_id OR game.opponent2ID = :user_id)", UserGamesEntity.class)
                    .setParameter("game_type", gameType)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message(String.format("Retrieved all games (%s) for the player.", gameType)).body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games for the player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesWonForPlayer(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.winnerID = :user_id", UserGamesEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved all games won for the player.").body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games for player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesWonForPlayerWithType(int userID, String gameType) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.winnerID = :user_id AND game.gameType = :game_type", UserGamesEntity.class)
                    .setParameter("game_type", gameType)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message(String.format("Retrieved all games (%s) won for the player.", gameType)).body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games for player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesLostForPlayer(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE (game.opponent1ID = :user_id OR game.opponent2ID = :user_id) AND game.winnerID != :user_id", UserGamesEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved all games lost for the player.").body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games lost for the player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesLostForPlayerWithType(int userID, String gameType) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.gameType = :game_type AND (game.opponent1ID = :user_id OR game.opponent2ID = :user_id) AND game.winnerID != :user_id", UserGamesEntity.class)
                    .setParameter("game_type", gameType)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message(String.format("Successfully retrieved all games (%s) lost for the player.", gameType)).body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games lost for the player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesTiedForPlayer(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE (game.opponent1ID = :user_id OR game.opponent2ID = :user_id) AND game.winnerID is null", UserGamesEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved all games tied for the player.").body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games lost for the player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllGamesTiedForPlayerWithType(int userID, String gameType) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserGamesEntity> games = session.createQuery("SELECT game FROM UserGamesEntity game WHERE game.gameType = :game_type AND (game.opponent1ID = :user_id OR game.opponent2ID = :user_id) AND game.winnerID is null", UserGamesEntity.class)
                    .setParameter("game_type", gameType)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.message(String.format("Successfully retrieved all games (%s) tied for the player.", gameType)).body("games", games));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all games lost for the player: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> addGame(CreateGameRequest request) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserGamesEntity game = new UserGamesEntity();
            game.setOpponent1ID(request.getOpponent1ID());
            game.setOpponent2ID(request.getOpponent2ID());
            game.setWinnerID(request.getWinnerID());
            game.setGameType(request.getGameType());

            session.beginTransaction();
            session.persist(game);
            session.getTransaction().commit();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Response.of("Successfully added game."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error created the game: %s", e.getMessage()));
        }
    }

}
