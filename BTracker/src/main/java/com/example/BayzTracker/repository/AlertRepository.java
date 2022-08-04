package com.example.BayzTracker.repository;

import com.example.BayzTracker.model.entity.Alert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    Optional<Alert> findById(int id);

    @Query(value = "SELECT * FROM alert where user_id = ?1", nativeQuery = true)
    List<Alert> getAlertByUserId(int userId, Pageable pageable);

    @Query(value = "SELECT * FROM alert where status = ?1", nativeQuery = true)
    List<Alert> getAlertByStatus(String status);
}
