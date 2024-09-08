package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "user_donations")
@Getter
@Setter
@JsonPropertyOrder(value = {"id", "sender_id", "receiver_id", "donation_time"})
public class UserDonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private int id;

    @Column(name = "sender_id")
    @JsonProperty("sender_id")
    private int senderID;

    @Column(name = "receiver_id")
    @JsonProperty("receiver_id")
    private int receiverID;

    @Column(name = "donation_time", insertable = false)
    @JsonProperty("donation_time")
    private Timestamp donationTime;

}
