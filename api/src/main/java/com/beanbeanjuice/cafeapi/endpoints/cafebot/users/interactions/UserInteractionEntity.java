package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.interactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "user_interactions")
@Getter
@Setter
@JsonPropertyOrder(value = {
        "id", "sender_id", "receiver_id",
        "type", "timestamp"
})
public class UserInteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_id")
    @JsonProperty("sender_id")
    private int senderID;

    @Column(name = "receiver_id")
    @JsonProperty("receiver_id")
    private int receiverID;

    @Column(name = "type")
    @JsonProperty("type")
    private String type;

    @Column(name = "timestamp", insertable = false)
    @JsonProperty("timestamp")
    private Timestamp timestamp;

}
