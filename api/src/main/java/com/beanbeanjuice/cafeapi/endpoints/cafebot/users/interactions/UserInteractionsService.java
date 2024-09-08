package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.interactions;

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
public class UserInteractionsService {

    @Transactional
    public ResponseEntity<?> getAllInteractions() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    "SELECT interaction from UserInteractionEntity interaction",
                    UserInteractionEntity.class
            ).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsByType(String type) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    "SELECT interaction from UserInteractionEntity interaction WHERE interaction.type = :type",
                    UserInteractionEntity.class
            ).setParameter("type", type).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsByTypeForUser(int userID, String type) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                    SELECT interaction from UserInteractionEntity interaction
                    WHERE interaction.type = :type AND (interaction.senderID = :user_id OR interaction.receiverID = :user_id)
                    """,
                    UserInteractionEntity.class
            ).setParameter("type", type).setParameter("user_id", userID).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsForUser(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                                SELECT interaction from UserInteractionEntity interaction
                                WHERE interaction.senderID = :user_id OR interaction.receiverID = :user_id
                               """,
                    UserInteractionEntity.class
            ).setParameter("user_id", userID).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions for user: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsSentForUser(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                                SELECT interaction from UserInteractionEntity interaction
                                WHERE interaction.senderID = :user_id
                               """,
                    UserInteractionEntity.class
            ).setParameter("user_id", userID).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions send for user: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsSentByTypeForUser(int userID, String type) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                                SELECT interaction from UserInteractionEntity interaction
                                WHERE interaction.senderID = :user_id AND interaction.type = :type
                               """,
                    UserInteractionEntity.class
            ).setParameter("user_id", userID).setParameter("type", type).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions sent for user: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsReceivedForUser(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                                SELECT interaction from UserInteractionEntity interaction
                                WHERE interaction.receiverID = :user_id
                               """,
                    UserInteractionEntity.class
            ).setParameter("user_id", userID).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions received for user: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> getAllInteractionsReceivedByTypeForUser(int userID, String type) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserInteractionEntity> interactions = session.createQuery(
                    """
                                SELECT interaction from UserInteractionEntity interaction
                                WHERE interaction.receiverID = :user_id AND interaction.type = :type
                               """,
                    UserInteractionEntity.class
            ).setParameter("user_id", userID).setParameter("type", type).getResultList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            Response
                                    .message("Successfully retrieved interactions.")
                                    .body("interactions", interactions)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an error getting all interactions received for user: %s", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> createInteraction(int senderID, int receiverID, String type) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserInteractionEntity interaction = new UserInteractionEntity();
            interaction.setSenderID(senderID);
            interaction.setReceiverID(receiverID);
            interaction.setType(type);

            session.beginTransaction();
            session.persist(interaction);
            session.getTransaction().commit();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Response.of(String.format("Interaction (%d) created successfully.", interaction.getId())));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("There was an creating the interaction: %s", e.getMessage()));
        }
    }

}
