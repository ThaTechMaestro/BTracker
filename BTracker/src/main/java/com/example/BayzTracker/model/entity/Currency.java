package com.example.BayzTracker.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "currency")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(exclude = {"createdTime", "updatedTime"})
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String name;

    String symbol;

    BigDecimal currentPrice;

    @JsonIgnore
    @CreationTimestamp
    private Instant createdTime;

    @JsonIgnore
    @CreationTimestamp
    private Instant updatedTime;
}
