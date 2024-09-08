package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import com.beanbeanjuice.cafeapi.endpoints.cafe.user.CafeUserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "refresh_tokens", schema = "cafe-v2", catalog = "cafe-v2")
@Getter
@Setter
@JsonPropertyOrder(value = {"id", "refresh_token", "expiration_date", "user"})
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "refresh_token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Column(name = "expiration_date")
    @JsonProperty("expiration_date")
    private Timestamp expirationDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CafeUserEntity user;

}
