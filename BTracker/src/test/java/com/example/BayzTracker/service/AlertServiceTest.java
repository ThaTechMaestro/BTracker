package com.example.BayzTracker.service;

import com.example.BayzTracker.AbstractIntegrationTest;
import com.example.BayzTracker.exception.ResourceNotFoundException;
import com.example.BayzTracker.model.entity.Alert;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.model.enums.AlertStatus;
import com.example.BayzTracker.model.enums.Role;
import com.example.BayzTracker.model.payload.AlertRequest;
import com.example.BayzTracker.repository.AlertRepository;
import com.example.BayzTracker.repository.CurrencyRepository;
import com.example.BayzTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class AlertServiceTest extends AbstractIntegrationTest {


    @Autowired
    AlertService alertService;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UtilService utilService;

    @AfterEach
    void cleanUp(){
        currencyRepository.deleteAll();
        alertRepository.deleteAll();
    }

    User createAuthUser(){
        return userRepository.save(User.builder()
                .userName("Admin")
                .password("admin")
                .role(Role.ADMIN.name())
                .build());
    }

    private Currency createCurrency(String name, String symbol, BigDecimal price){

        return currencyRepository.save(Currency.builder()
                .name(name)
                .symbol(symbol)
                .currentPrice(price)
                .build());
    }

    private AlertRequest createAlertRequest(){
        AlertRequest alertRequest = new AlertRequest();

        alertRequest.setCurrencySymbol("BTC");
        alertRequest.setTargetedPrice(BigDecimal.valueOf(80));

        return alertRequest;
    }

    @Test
    void shouldCreateAlert(){
        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());

        Alert alert = alertService.createAlert(alertRequest);

        assertThat(alert.getUserId()).isEqualTo(authUser.getUserId());
        assertThat(alert.getCurrencySymbol()).isEqualTo("BTC");
        assertTrue(alert.getTargetedPrice().compareTo(BigDecimal.valueOf(80)) == 0);


        alertRequest.setTargetedPrice(BigDecimal.valueOf(201));
        alertRequest.setCurrencySymbol("BTH");
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            alertService.createAlert(alertRequest);});

        String expectedMessage = "Currency not listed on this platform";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldEditAlert(){

        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());

        Alert alert = alertService.createAlert(alertRequest);

        alertRequest.setTargetedPrice(BigDecimal.valueOf(150));

        Alert editedAlert = alertService.editAlert(alert.getId(), alertRequest);
        assertTrue(editedAlert.getTargetedPrice().compareTo(BigDecimal.valueOf(150)) == 0);
    }

    @Test
    void shouldDeleteAlert(){
        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());

        Alert alert = alertService.createAlert(alertRequest);
        alertService.deleteAlert(alert.getId());

        List<Alert> alertByUserId = alertService.fetchAlertForUser(authUser.getUserId(), Pageable.unpaged());
        assertTrue(alertByUserId.isEmpty());
    }

    @Test
    void shouldCancelAlert(){

        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());
        Alert alert = alertService.createAlert(alertRequest);
        Alert cancelledAlert = alertService.cancelAlert(alert.getId());

        assertTrue(cancelledAlert.getStatus().equalsIgnoreCase(AlertStatus.CANCELLED.name()));
    }

    @Test
    void shouldAcceptAlert(){

        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());
        Alert alert = alertService.createAlert(alertRequest);

        alertRequest.setTargetedPrice(new BigDecimal(200));
        alertService.editAlert(alert.getId(), alertRequest);
        utilService.scheduleAlertNotification();

        Alert acceptedAlert = alertService.acceptAlert(alert.getId());
        assertTrue(acceptedAlert.getStatus().equalsIgnoreCase(AlertStatus.ACKED.name()));
    }

    @Test
    void shouldFetchAlertForUser(){

        User authUser = createAuthUser();
        createCurrency("Bitcoin", "BTC", new BigDecimal(200));
        AlertRequest alertRequest = createAlertRequest();
        alertRequest.setUserId(authUser.getUserId());
        alertService.createAlert(alertRequest);

        List<Alert> alertByUserId = alertService.fetchAlertForUser(alertRequest.getUserId(), Pageable.unpaged());
        assertThat(alertByUserId.size()).isEqualTo(1);
    }
}
