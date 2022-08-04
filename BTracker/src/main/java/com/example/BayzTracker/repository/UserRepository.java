package com.example.BayzTracker.repository;

import com.example.BayzTracker.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query(value = "select * from users", nativeQuery = true)
    List<User> fetchAll(Pageable pageable);

    @Query(value = "select * from users where user_id = ?1", nativeQuery = true)
    Optional<User> findById(int id);
}
