package com.example.BayzTracker.repository;

import com.example.BayzTracker.model.entity.UnsupportedCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnsupportedCurrencyRepository extends JpaRepository<UnsupportedCurrency, Long> {

    @Query(value="select symbol from unsupported_currency",nativeQuery = true)
    List<String> fetchAllUnsuppportedCurrencySymbols();
}
