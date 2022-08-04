package com.example.BayzTracker.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "alert")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(exclude = {"createdTime", "updatedTime"})
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String currencySymbol;

    String status;

    BigDecimal targetedPrice;

    int userId;

    @JsonIgnore
    @CreationTimestamp
    private Instant createdTime;

    @JsonIgnore
    @CreationTimestamp
    private Instant updatedTime;
}
