package com.example.BayzTracker.model.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class RegisterCurrencyRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 40, message = "Length should be between 3 and 40.")
    String name;

    @NotBlank(message = "Symbol cannot be blank")
    @Size(min = 1, max = 6, message = "Length should be between 1 and 6.")
    String symbol;

    @NotBlank(message = "Price cannot be blank")
    @Positive
    BigDecimal price;
}
