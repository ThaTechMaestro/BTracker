package com.example.BayzTracker.service;


import com.example.BayzTracker.AbstractIntegrationTest;
import com.example.BayzTracker.exception.UnsupportedCurrencyCreationException;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.entity.User;
import com.example.BayzTracker.model.enums.Role;
import com.example.BayzTracker.model.payload.RegisterCurrencyRequest;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class CurrencyServiceTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrencyService currencyService;

    @AfterEach
    void cleanUp(){
        currencyRepository.deleteAll();
        userRepository.deleteAll();
    }

    private RegisterCurrencyRequest createRegisterCurrencyRequest(){
        RegisterCurrencyRequest currencyRequest = new RegisterCurrencyRequest();

        currencyRequest.setName("Bitcoin");
        currencyRequest.setPrice(BigDecimal.valueOf(100));
        currencyRequest.setSymbol("BTC");

        return currencyRequest;
    }

    private void createAuthUser(){
        userRepository.save(User.builder()
                .userName("Admin")
                .password("admin")
                .role(Role.ADMIN.name())
                .build());

        userRepository.save(User.builder()
                .userName("User")
                .password("user")
                .role(Role.USER.name())
                .build());

    }

    @Test
    void shouldCreateCurrency(){
        createAuthUser();
        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();

        Currency currency = currencyService.registerCurrency(currencyRequest);

        assertTrue(currency.getName().equalsIgnoreCase("Bitcoin"));
        assertTrue(currency.getSymbol().equalsIgnoreCase("BTC"));
        assertTrue(currency.getCurrentPrice().compareTo(BigDecimal.valueOf(100)) == 0);

        currencyRequest.setName("Ethereum");
        currencyRequest.setSymbol("ETH");
        currencyRequest.setPrice(BigDecimal.valueOf(140));

        Exception exception = assertThrows(UnsupportedCurrencyCreationException.class, () -> {
            currencyService.registerCurrency(currencyRequest);});

        String expectedMessage = "Currency not supported";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldFetchAllCurrency(){

        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();

        currencyService.registerCurrency(currencyRequest);

        List<Currency> currencyList = currencyService.fetchAllCurrency(Pageable.unpaged());
        assertThat(currencyList.size()).isEqualTo(1);

        Currency currency = currencyList.get(0);
        assertTrue(currency.getName().equalsIgnoreCase("Bitcoin"));
        assertTrue(currency.getSymbol().equalsIgnoreCase("BTC"));
        assertTrue(currency.getCurrentPrice().compareTo(BigDecimal.valueOf(100)) == 0);
    }

    @Test
    void shouldEditCurrency(){

        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();

        Currency oldCurrency = currencyService.registerCurrency(currencyRequest);

        currencyRequest.setName("Bitcoin");
        currencyRequest.setSymbol("BTC");
        currencyRequest.setPrice(BigDecimal.valueOf(5000));

        Currency newCurrency = currencyService.editCurrency(oldCurrency.getId(), currencyRequest);

        assertTrue(newCurrency.getCurrentPrice().compareTo(oldCurrency.getCurrentPrice()) == 1);
        assertTrue(newCurrency.getCurrentPrice().compareTo(BigDecimal.valueOf(5000)) == 0);
    }

    @Test
    void shouldDeleteCurrency(){

        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();
        Currency currency = currencyService.registerCurrency(currencyRequest);
        currencyService.removeCurrency(currency);

        List<Currency> currencyList = currencyService.fetchAllCurrency(Pageable.unpaged());
        assertThat(currencyList.size()).isEqualTo(0);
    }

    @Test
    void shouldFetchCurrencyByName(){

        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();
        Currency currency = currencyService.registerCurrency(currencyRequest);

        Optional<Currency> fetchedCurrency = currencyService.findByName(currency.getName());
        Currency currencyDetails = fetchedCurrency.get();
        assertThat(currencyDetails.getName()).isEqualTo(currency.getName());
    }

    @Test
    void shouldFetchCurrencyBySymbol(){

        RegisterCurrencyRequest currencyRequest = createRegisterCurrencyRequest();
        Currency currency = currencyService.registerCurrency(currencyRequest);

        Optional<Currency> fetchedCurrency = currencyService.findBySymbol(currency.getSymbol());
        Currency currencyDetails = fetchedCurrency.get();
        assertThat(currencyDetails.getSymbol()).isEqualTo(currency.getSymbol());
    }
}
