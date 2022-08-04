package com.example.BayzTracker.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "unsupported_currency")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class UnsupportedCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String name;

    String symbol;
}
