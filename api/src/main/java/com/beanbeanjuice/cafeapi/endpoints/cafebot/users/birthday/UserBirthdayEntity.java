package com.beanbeanjuice.cafeapi.endpoints.cafebot.users.birthday;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_birthdays")
@Getter
@Setter
@JsonPropertyOrder(value = {"user_id", "birth_date", "time_zone"})
public class UserBirthdayEntity {

    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    @JsonIgnore
    private int userID;

    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    private String birthDate;

    @Column(name = "time_zone")
    @JsonProperty("time_zone")
    private String timeZone;

}
