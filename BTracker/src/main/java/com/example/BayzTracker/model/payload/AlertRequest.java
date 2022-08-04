package com.example.BayzTracker.model.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class AlertRequest {

    @NotEmpty
    String currencySymbol;

    @Positive
    BigDecimal targetedPrice;

    @Positive
    int userId;
}
