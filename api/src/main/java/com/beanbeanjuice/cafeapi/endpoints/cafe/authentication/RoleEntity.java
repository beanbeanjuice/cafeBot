package com.beanbeanjuice.cafeapi.endpoints.cafe.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles", schema = "cafe-v2", catalog = "cafe-v2")
@Getter
@Setter
@JsonPropertyOrder(value = {"id", "role_name"})
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role")
    @JsonProperty("role_name")
    private String roleName;

    @Override
    public String toString() {
        return String.format("%d: %s", id, roleName);
    }

}
