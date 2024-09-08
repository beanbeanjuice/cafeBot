package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.code;

import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCodeService {

    @Transactional
    public ResponseEntity<?> generateCode(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserCodeEntity userCodeEntity = session.get(UserCodeEntity.class, userID);

            if (userCodeEntity == null) userCodeEntity = new UserCodeEntity();

            userCodeEntity.setUserID(userID);
            userCodeEntity.generateRandomCode();

            session.beginTransaction();
            session.persist(userCodeEntity);
            session.getTransaction().commit();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Response.message("Successfully generated code.").body("code", userCodeEntity.getCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.of(String.format("There was an error generating a code for that user: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> getCode(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserCodeEntity userCodeEntity = session.get(UserCodeEntity.class, userID);

            if (userCodeEntity == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.of("User code not found."));

            return ResponseEntity.status(HttpStatus.OK).body(
                    Response.message("User code found.").body("code", userCodeEntity.getCode())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Error getting user code: %s", e.getMessage())));
        }
    }

}
