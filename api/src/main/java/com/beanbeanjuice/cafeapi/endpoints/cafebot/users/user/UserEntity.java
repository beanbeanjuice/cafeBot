package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.user;

import com.beanbeanjuice.cafeapi.endpoints.cafebot.users.birthday.UserBirthdayEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafebot.users.code.UserCodeEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafebot.users.donation.UserDonationEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafebot.users.games.UserGamesEntity;
import com.beanbeanjuice.cafeapi.endpoints.cafebot.users.interactions.UserInteractionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@JsonPropertyOrder(value = {
        "id", "user_snowflake_id", "code",
        "balance", "birthday", "donations",
        "games_played", "interactions"
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_snowflake_id")
    @JsonProperty("user_snowflake_id")
    private long userSnowflakeID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @JsonProperty("code")
    private UserCodeEntity code;

    @Column(name = "balance")
    @JsonProperty("balance")
    private double balance;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @JsonProperty("birthday")
    private UserBirthdayEntity birthday;

    @OneToMany(mappedBy = "senderID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserDonationEntity> donationsSent;

    @OneToMany(mappedBy = "receiverID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserDonationEntity> donationsReceived;

    @Transient
    @JsonProperty("donations")
    private Set<UserDonationEntity> donations;

    @OneToMany(mappedBy = "opponent1ID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserGamesEntity> gamesPlayedAsPlayer1;

    @OneToMany(mappedBy = "opponent2ID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserGamesEntity> gamesPlayedAsPlayer2;

    @Transient
    @JsonProperty("games_played")
    private Set<UserGamesEntity> gamesPlayed;

    @OneToMany(mappedBy = "senderID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserInteractionEntity> interactionsSent;

    @OneToMany(mappedBy = "receiverID", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserInteractionEntity> interactionsReceived;

    @Transient
    @JsonProperty("interactions")
    private Set<UserInteractionEntity> interactions;

    @PostLoad
    private void postLoad() {
        this.donations = new HashSet<>();
        this.donations.addAll(this.donationsReceived);
        this.donations.addAll(this.donationsSent);

        this.gamesPlayed = new HashSet<>();
        this.gamesPlayed.addAll(this.gamesPlayedAsPlayer1);
        this.gamesPlayed.addAll(this.gamesPlayedAsPlayer2);

        this.interactions = new HashSet<>();
        this.interactions.addAll(this.interactionsReceived);
        this.interactions.addAll(this.interactionsSent);
    }

}
