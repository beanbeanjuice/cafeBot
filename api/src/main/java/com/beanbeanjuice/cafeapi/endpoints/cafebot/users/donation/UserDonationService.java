package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.donation;

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
public class UserDonationService {

    @Transactional
    public ResponseEntity<?> getAllDonations() {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserDonationEntity> donations = session.createQuery("SELECT donation from UserDonationEntity donation", UserDonationEntity.class).getResultList();

            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Successfully retrieved all donations.").body("donations", donations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error getting all donations: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> getDonationsSent(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserDonationEntity> donations = session
                    .createQuery("SELECT donation from UserDonationEntity donation WHERE donation.senderID = :user_id", UserDonationEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Donations retrieved successfully.").body("donations_sent", donations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error getting donations sent: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> getDonationsReceived(int userID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            List<UserDonationEntity> donations = session
                    .createQuery("SELECT donation from UserDonationEntity donation WHERE donation.receiverID = :user_id", UserDonationEntity.class)
                    .setParameter("user_id", userID)
                    .getResultList();

            return ResponseEntity.status(HttpStatus.OK).body(Response.message("Donations retrieved successfully.").body("donations_received", donations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error getting donations received: %s", e.getMessage())));
        }
    }

    @Transactional
    public ResponseEntity<?> addDonation(int senderID, int receiverID) {
        try (Session session = DatabaseService.getSessionFactory().openSession()) {
            UserDonationEntity donation = new UserDonationEntity();
            donation.setSenderID(senderID);
            donation.setReceiverID(receiverID);

            session.beginTransaction();
            session.persist(donation);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.CREATED).body(Response.of("Successfully added donation."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.of(String.format("There was an error adding a donation: %s", e.getMessage())));
        }
    }

}
