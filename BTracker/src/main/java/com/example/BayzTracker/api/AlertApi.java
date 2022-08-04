package com.example.BayzTracker.api;

import com.example.BayzTracker.exception.UserExistenceException;
import com.example.BayzTracker.model.entity.Alert;
import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.model.payload.AlertRequest;
import com.example.BayzTracker.service.AlertService;
import com.example.BayzTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/v1/alert")
@Validated
@RequiredArgsConstructor
public class AlertApi {

    private final UserService userService;
    private final AlertService alertService;

    @Operation(summary = "Create an Alert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alert> addAlert(@RequestBody AlertRequest alertRequest){

        Optional<User> user = userService.fetchUserById(alertRequest.getUserId());
        if (user.isEmpty()){
            throw new UserExistenceException("User does not exist, kindly register");
        }

        Alert createdAlert = alertService.createAlert(alertRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAlert);
    }

    @Operation(summary = "Edit an Alert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @PutMapping(value = "/edit/{alertId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alert> modifyAlert(@PathVariable("alertId") int alertId, @RequestBody AlertRequest alertRequest){
        return ResponseEntity.ok().body(alertService.editAlert(alertId, alertRequest));
    }

    @Operation(summary = "Remove an Alert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @DeleteMapping(value = "/remove/{alertId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alert> removeAlert(@PathVariable("alertId") int alertId){

        alertService.deleteAlert(alertId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Cancel an Alert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @PostMapping(value="/cancel/{alertId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alert> cancelAlert(@PathVariable("alertId") int alertId){

        Alert cancelledAlert = alertService.cancelAlert(alertId);
        return ResponseEntity.ok().body(cancelledAlert);
    }

    @Operation(summary = "Acknowledge an Alert")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @PostMapping(value="/accept/{alertId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alert> acceptAlert(@PathVariable("alertId") int alertId){

        Alert acceptedAlert = alertService.acceptAlert(alertId);
        return ResponseEntity.ok().body(acceptedAlert);
    }

    @Operation(summary = "Fetch all Alert for a particular user")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping(value = "/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Alert>> fetchAlertForUser(@PathVariable("userId") int userId, Pageable pageable){

        Optional<User> user = userService.fetchUserById(userId);
        if (user.isEmpty()){
            throw new UserExistenceException("User does not exist, kindly register");
        }

        List<Alert> alertList = alertService.fetchAlertForUser(userId, pageable);
        return ResponseEntity.status(HttpStatus.FOUND).body(alertList);
    }
}
