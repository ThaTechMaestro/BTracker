package com.example.BayzTracker.service;

import com.example.BayzTracker.exception.ResourceNotFoundException;
import com.example.BayzTracker.exception.UnsupportedCurrencyCreationException;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.payload.RegisterCurrencyRequest;
import com.example.BayzTracker.repository.CurrencyRepository;
import com.example.BayzTracker.repository.UnsupportedCurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CurrencyService {

    private final UnsupportedCurrencyRepository unsupportedCurrencyRepository;
    private final CurrencyRepository currencyRepository;

    public CurrencyService (UnsupportedCurrencyRepository unsupportedCurrencyRepository, CurrencyRepository currencyRepository){
        this.unsupportedCurrencyRepository = unsupportedCurrencyRepository;
        this.currencyRepository = currencyRepository;

    }

    public List<Currency> fetchAllCurrency(Pageable pageable){
        return this.currencyRepository.fetchAllCurrency(pageable);
    }

    public Currency registerCurrency(RegisterCurrencyRequest currencyRequest){

        unsupportedCurrencyCheck(currencyRequest.getName().toUpperCase(), currencyRequest.getSymbol().toUpperCase());
        Currency currency = Currency.builder()
                .name(currencyRequest.getName().toUpperCase())
                .symbol(currencyRequest.getSymbol().toUpperCase())
                .currentPrice(currencyRequest.getPrice())
                .build();
        this.currencyRepository.save(currency);
        return currency;
    }

    public Currency editCurrency(int currencyId, RegisterCurrencyRequest currencyRequest){

        Optional<Currency> currency = currencyRepository.findById(currencyId);

        if (currency.isEmpty()){
            throw new ResourceNotFoundException("Currency not listed on this platform");
        }

        Currency currencyDetails = currency.get();

        currencyDetails.setName(currencyRequest.getName().toUpperCase());
        currencyDetails.setSymbol(currencyRequest.getSymbol().toUpperCase());
        currencyDetails.setCurrentPrice(currencyRequest.getPrice());

        return currencyRepository.save(currencyDetails);
    }

    public Currency removeCurrency(Currency currency){
        this.currencyRepository.delete(currency);
        return currency;
    }

    public Optional<Currency> findById(int currencyId){
        return this.currencyRepository.findById(currencyId);
    }

    public Optional<Currency> findByName(String currencyName){
        return this.currencyRepository.findByName(currencyName.toUpperCase());
    }

    public Optional<Currency> findBySymbol(String currencySymbol){
        return this.currencyRepository.findBySymbol(currencySymbol.toUpperCase());
    }

    public void  unsupportedCurrencyCheck(String name, String symbol){
        List<String> unsupportedCurrencySymbolList = this.unsupportedCurrencyRepository.fetchAllUnsuppportedCurrencySymbols();
        if (unsupportedCurrencySymbolList.contains(symbol)){
            log.warn("Currency with name:{} and symbol:{} not supported.", name, symbol);
            throw new UnsupportedCurrencyCreationException("Currency not supported");
        }
    }
}
