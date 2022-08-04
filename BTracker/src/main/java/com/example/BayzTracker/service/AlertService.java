package com.example.BayzTracker.service;

import com.example.BayzTracker.exception.AlertStatusException;
import com.example.BayzTracker.exception.EqualPriceException;
import com.example.BayzTracker.exception.ResourceNotFoundException;
import com.example.BayzTracker.model.entity.Alert;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.enums.AlertStatus;
import com.example.BayzTracker.model.payload.AlertRequest;
import com.example.BayzTracker.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final CurrencyService currencyService;

    public Alert createAlert(AlertRequest alertRequest){

        Currency currencyDetails = validateCurrencySymbol(alertRequest.getCurrencySymbol().toUpperCase());
        if(alertRequest.getTargetedPrice().compareTo(currencyDetails.getCurrentPrice()) == 0){
            throw new EqualPriceException("Alert and currency price are equal, kindly change alert price");
        }

        Alert alert = Alert.builder()
                .currencySymbol(alertRequest.getCurrencySymbol().toUpperCase())
                .status(AlertStatus.NEW.name())
                .targetedPrice(alertRequest.getTargetedPrice())
                .userId(alertRequest.getUserId())
                .build();
        return alertRepository.save(alert);
    }

    public Alert editAlert(int alertId, AlertRequest alertRequest){

        Alert alertDetails = validateAlertByAlertId(alertId);
        alertDetails.setCurrencySymbol(alertRequest.getCurrencySymbol().toUpperCase());
        alertDetails.setTargetedPrice(alertRequest.getTargetedPrice());

        return alertRepository.save(alertDetails);
    }

    public Alert deleteAlert(int alertId){

        Alert alert = validateAlertByAlertId(alertId);
        alertRepository.delete(alert);
        return alert;
    }

    public Alert cancelAlert(int alertId){

        Alert alertDetails = validateAlertByAlertId(alertId);
        if (alertDetails.getStatus().equalsIgnoreCase(AlertStatus.TRIGGERRED.name())){
            throw new AlertStatusException("Alert has been sent, can not be cancelled");
        }

        alertDetails.setStatus(AlertStatus.CANCELLED.name());
        return alertRepository.save(alertDetails);
    }

    public Alert acceptAlert(int alertId){

        Alert alertDetails = validateAlertByAlertId(alertId);
        if (alertDetails.getStatus().equalsIgnoreCase(AlertStatus.TRIGGERRED.name())){
            alertDetails.setStatus(AlertStatus.ACKED.name());
            return alertRepository.save(alertDetails);
        }

        throw new AlertStatusException("Current Alert status not met");
    }

    public List<Alert> fetchAlertForUser(int userId, Pageable pageable){
        return alertRepository.getAlertByUserId(userId, pageable);
    }

    public Alert validateAlertByAlertId(int alertId){
        Optional<Alert> alert = alertRepository.findById(alertId);
        if(alert.isEmpty()){
            throw new ResourceNotFoundException("Alert not found");
        }
        return alert.get();
    }

    public Currency validateCurrencySymbol(String currencySymbol){

        Optional<Currency> currency = currencyService.findBySymbol(currencySymbol);
        if (currency.isEmpty()){
            throw new ResourceNotFoundException("Currency not listed on this platform");
        }
        return currency.get();
    }
}
