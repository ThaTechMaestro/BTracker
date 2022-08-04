package com.example.BayzTracker.service;

import com.example.BayzTracker.model.entity.Alert;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.model.enums.AlertStatus;
import com.example.BayzTracker.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilService {

    private final AlertRepository alertRepository;
    private final CurrencyService currencyService;
    private final UserService userService;

    public List<Alert> fetchAlertByStatus(String status){

        List<Alert> alertList = alertRepository.getAlertByStatus(status);
        List<Alert> triggeredAlertList = alertList.stream().filter(alert ->  setAlertTriggerStatus(alert) == true).collect(Collectors.toList());
        return triggeredAlertList;
    }

    public boolean setAlertTriggerStatus(Alert alert) {

        Optional<Currency> currency = currencyService.findBySymbol(alert.getCurrencySymbol());

        if (currency.isEmpty()){
            return false;
        }

        Currency currencyDetails = currency.get();
        if (alert.getTargetedPrice().compareTo(currencyDetails.getCurrentPrice()) == 0) {
            alert.setStatus(AlertStatus.TRIGGERRED.name());
            alertRepository.save(alert);
            return true;
        }
        return false;
    }

    public void sendNotification(List<Alert> alertList){
        alertList.parallelStream().forEach(alert -> sendNotificationMessage(alert));
    }

    public void sendNotificationMessage(Alert alert){

        Optional<User> user = userService.fetchUserById(alert.getUserId());
        User userDetails = user.get();
        log.info("Hello {}, {} has reached Price: {}!", userDetails.getUserName(), alert.getCurrencySymbol(), alert.getTargetedPrice());
    }

    @Scheduled(fixedRate=30000)
    public void scheduleAlertNotification(){
        List<Alert> newAlertsList = fetchAlertByStatus(AlertStatus.NEW.name());
        sendNotification(newAlertsList);
    }
}
