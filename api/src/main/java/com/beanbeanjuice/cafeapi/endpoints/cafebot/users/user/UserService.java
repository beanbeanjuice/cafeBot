package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.user;

import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    @Transactional
    public ResponseEntity<?> getAllUsers() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserEntity> users = session.createQuery("SELECT user from UserEntity user", UserEntity.class).getResultList();

            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Successfully retrieved all users.").body("users", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error getting all users: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> getUser(final long userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserEntity user = session.get(UserEntity.class, userID);

            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Successfully retrieved user.").body("user", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Error getting user from database: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> addUserFromSnowflake(final long userSnowflakeID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            session.beginTransaction();

            UserEntity user = new UserEntity();
            user.setUserSnowflakeID(userSnowflakeID);
            session.persist(user);

            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.CREATED).body(Response.of(String.format("Successfully created (%d) with ID (%d)", userSnowflakeID, user.getId())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.of(String.format("Error adding user to database: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteUser(final int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {

            Map<String, Object> deletedResponse = Response.of("User has been successfully removed.");

            UserEntity user = session.get(UserEntity.class, userID);
            if (user == null) return ResponseEntity.status(HttpStatus.OK).body(deletedResponse);

            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK).body(deletedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.message(String.format("Error removing user from database: %s", e.getMessage())));
        }
    }

}
