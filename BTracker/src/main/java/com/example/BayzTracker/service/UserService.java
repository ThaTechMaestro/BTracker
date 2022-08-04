package com.example.BayzTracker.service;

import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> fetchUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> fetchAll(Pageable pageable){
        return userRepository.fetchAll(pageable);
    }
}
