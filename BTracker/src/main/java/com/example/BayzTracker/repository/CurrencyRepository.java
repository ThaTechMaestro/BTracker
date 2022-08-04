package com.example.BayzTracker.repository;

import com.example.BayzTracker.model.entity.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findById(int id);

    @Query(value = "select * from currency",nativeQuery = true)
    List<Currency> fetchAllCurrency(Pageable pageable);

    Optional<Currency> findByName(String name);

    Optional<Currency> findBySymbol(String symbol);

}
