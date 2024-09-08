package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.birthday;

import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserBirthdayService {

    @Transactional
    public ResponseEntity<?> getBirthday(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserBirthdayEntity birthday = session.get(UserBirthdayEntity.class, userID);

            if (birthday == null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.of("User does not have a birthday set."));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.message("Successfully retrieved user's birthday.").body("birthday", birthday));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.of(String.format("There was an error retrieving the user's birthday: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> updateBirthday(int userID, CreateBirthdayRequest request) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserBirthdayEntity birthday = new UserBirthdayEntity();
            birthday.setUserID(userID);
            birthday.setBirthDate(request.getDate());
            birthday.setTimeZone(request.getTimeZone());

            session.beginTransaction();
            session.merge(birthday);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.of(String.format("Successfully set user (%d)'s birthday to %s on %s.", userID, request.getDate(), request.getTimeZone())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("Error setting user's birthday: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteBirthday(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserBirthdayEntity birthday = session.get(UserBirthdayEntity.class, userID);

            Map<String, Object> successfulResponse = Response.of("Successfully deleted the user's birthday.");

            if (birthday == null) return ResponseEntity.status(HttpStatus.OK).body(successfulResponse);

            session.beginTransaction();
            session.remove(birthday);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK).body(successfulResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error deleting the user's birthday: %s", e.getMessage())));
        }
    }

}
