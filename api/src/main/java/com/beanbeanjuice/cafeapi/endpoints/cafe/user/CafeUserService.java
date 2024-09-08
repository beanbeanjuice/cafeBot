package com.beanbeanjuice.cafeapi.endpoints.cafe.user;

import com.beanbeanjuice.cafeapi.endpoints.cafe.authentication.CreateRequest;
import com.beanbeanjuice.cafeapi.endpoints.cafe.authentication.CreateResponse;
import com.beanbeanjuice.cafeapi.model.Response;
import com.beanbeanjuice.cafeapi.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeUserService {

    @Transactional
    public Optional<CafeUserEntity> findByUsername(final String username) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {

            session.beginTransaction();
            CafeUserEntity user = session.createQuery("SELECT user FROM CafeUserEntity user WHERE user.username = :username", CafeUserEntity.class)
                    .setParameter("username", username).getSingleResult();
            session.getTransaction().commit();

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public ResponseEntity<?> createUser(final CreateRequest request, final PasswordEncoder passwordEncoder) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {

            CafeUserEntity user = new CafeUserEntity();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());

            if (request.getLastName() == null || request.getLastName().isBlank() || request.getLastName().isEmpty())
                user.setLastName(request.getLastName());

            user.setPassword(passwordEncoder.encode(request.getPassword()));

            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.message("User created successfully.").body("user", new CreateResponse(user.getId(), user.getUsername())));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.of("A user with that name already exists."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error creating the user: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllUsers() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<CafeUserEntity> users = session.createQuery("SELECT user FROM CafeUserEntity user", CafeUserEntity.class).getResultList();
            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Successfully retrieved all users.").body("users", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error retrieving all users: %s", e.getMessage())));
        }
    }
}
