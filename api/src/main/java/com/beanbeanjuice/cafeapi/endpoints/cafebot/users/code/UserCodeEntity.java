package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Entity
@Table(name = "user_codes")
@Getter
@Setter
@JsonPropertyOrder(value = {"user_id", "code"})
public class UserCodeEntity {

    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    @JsonIgnore
    private int userID;

    @Column(name = "code")
    @JsonProperty("code")
    private String code;

    public void generateRandomCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        this.code = salt.toString();
    }

}
