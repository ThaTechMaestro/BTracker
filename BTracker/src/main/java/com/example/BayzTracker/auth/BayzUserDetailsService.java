package com.example.BayzTracker.auth;

import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BayzUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> registeredUser = userRepository.findByUserName(username);

        if (!registeredUser.isPresent()){
            throw new UsernameNotFoundException(username + "not found!");
        }
        return new BayzUserDetails(registeredUser.get());
    }
}
