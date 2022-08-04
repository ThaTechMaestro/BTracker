package com.example.BayzTracker.api;

import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @Operation(summary = "Fetch All Users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> fetchAllUsers(Pageable pageable){

        List<User> userList = userService.fetchAll(pageable);
        return ResponseEntity.ok().body(userList);
    }
}
