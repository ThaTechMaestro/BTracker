package com.example.BayzTracker.api;

import com.example.BayzTracker.exception.ResourceNotFoundException;
import com.example.BayzTracker.model.entity.Currency;
import com.example.BayzTracker.model.payload.RegisterCurrencyRequest;
import com.example.BayzTracker.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/v1/currency")
@RequiredArgsConstructor
public class CurrencyApi {

    private final CurrencyService currencyService;

    @Operation(summary = "Fetch All Currency")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Currency>> fetchAllCurrency(Pageable pageable){

        List<Currency> currencyList = currencyService.fetchAllCurrency(pageable);
        return ResponseEntity.ok().body(currencyList);
    }

    @Operation(summary = "Add a Currency")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Currency> addCurrency(@Valid @RequestBody RegisterCurrencyRequest registerCurrencyRequest){

        Currency registeredCurrency = currencyService.registerCurrency(registerCurrencyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCurrency);
    }

    @Operation(summary = "Edit a Currency")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/edit/{currencyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Currency> modifyCurrency(@PathVariable("currencyId") int currencyId, @RequestBody RegisterCurrencyRequest currencyRequest){
        return ResponseEntity.ok().body(currencyService.editCurrency(currencyId, currencyRequest));
    }

    @Operation(summary = "Remove a Currency")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value="/remove/{currencyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Currency> removeCurrency(@PathVariable int currencyId){

        Optional<Currency> registeredCurrency = currencyService.findById(currencyId);
        log.info("Registered Currency {}", registeredCurrency);

        if (!registeredCurrency.isPresent()){
            throw new ResourceNotFoundException("Currency not found");
        }

        currencyService.removeCurrency(registeredCurrency.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get current by name")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping(value="/search/by/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Currency> fetchCurrencyByName(@RequestParam("name") String currencyName){

        Optional<Currency> requestedCurrency = currencyService.findByName(currencyName);
        if (!requestedCurrency.isPresent()){
            throw new ResourceNotFoundException("Currency not found");
        }

        return new ResponseEntity<>(requestedCurrency.get(), HttpStatus.OK);
    }

    @Operation(summary = "Get currency by symbol")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping(value="/search/by/symbol", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Currency> fetchCurrencyBySymbol(@RequestParam("symbol") String currencySymbol){

        Optional<Currency> requestedCurrency = currencyService.findBySymbol(currencySymbol);
        if (!requestedCurrency.isPresent()){
            throw new ResourceNotFoundException("Currency not found");
        }

        return new ResponseEntity<>(requestedCurrency.get(), HttpStatus.OK);
    }

}
