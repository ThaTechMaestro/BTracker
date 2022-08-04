package com.example.BayzTracker.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(exclude = {"createdTime", "updatedTime"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String userName;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String role;

    @JsonIgnore
    @CreationTimestamp
    private Instant createdTime;

    @JsonIgnore
    @CreationTimestamp
    private Instant updatedTime;
}
