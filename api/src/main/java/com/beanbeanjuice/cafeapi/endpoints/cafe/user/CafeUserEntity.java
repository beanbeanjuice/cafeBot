package com.beanbeanjuice.cafeapi.endpoints.cafe.user;

import com.beanbeanjuice.cafeapi.endpoints.cafe.authentication.RoleEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", schema = "cafe-v2", catalog = "cafe-v2")
@Getter
@Setter
@JsonPropertyOrder(value = {"id", "username", "password", "first_name", "last_name", "user_roles"})
public class CafeUserEntity {

    public CafeUserEntity() {
        RoleEntity role = new RoleEntity();
        role.setId(1);
        role.setRoleName("USER");

        this.roles = Set.of(role);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            schema = "cafe-v2",
            catalog = "cafe-v2",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    public List<String> getRoles() {
        return roles.stream().map((role) -> String.format("ROLE_%s", role.getRoleName())).toList();
    }

    @Override
    public String toString() {
        return String.format("(%d) %s - %s", id, username, String.join(", ", this.getRoles()));
    }

}
