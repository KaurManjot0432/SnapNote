package com.manjot.snapnote.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Model class representing a User entity in MongoDB.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "users")
public class User {

    /**
     * The unique identifier of the user.
     */
    @Id
    private String id;

    /**
     * The username of the user, with unique indexing.
     */
    @NotNull
    @Indexed(unique = true)
    private String username;

    /**
     * The email of the user, with unique indexing and email validation.
     */
    @NotNull
    @Email
    @Indexed(unique = true)
    private String email;

    /**
     * The password of the user, with a minimum size requirement.
     */
    @NotNull
    @Size(min = 5)
    private String password;

    /**
     * Set of roles associated with the user.
     */
    @DBRef
    private Set<Role> roles = new HashSet<>();

    /**
     * The timestamp when the user was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
