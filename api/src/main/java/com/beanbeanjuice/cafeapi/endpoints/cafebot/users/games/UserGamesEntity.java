package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "user_games")
@Getter
@Setter
@JsonPropertyOrder(value = {"id", "opponent1_id", "opponent2_id", "created_at", "winnder_id", "game_type"})
public class UserGamesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "opponent1_id")
    @JsonProperty("opponent1_id")
    private int opponent1ID;

    @Column(name = "opponent2_id")
    @JsonProperty("opponent2_id")
    private int opponent2ID;

    @Column(name = "created_at", insertable = false)
    @JsonProperty("created_at")
    private Timestamp createdAt;

    @Nullable
    @Column(name = "winner_id")
    @JsonProperty("winner_id")
    private Integer winnerID;

    @Column(name = "game_type")
    @JsonProperty("game_type")
    private String gameType;

}
