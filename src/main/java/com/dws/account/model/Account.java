package com.dws.account.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @NotEmpty(message = "Account ID cannot be null or empty")
    private Long id;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0", message = "Balance cannot be negative")
    private BigDecimal balance;

}
