package com.dws.account.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    Long accountIdFrom;
    Long accountIdTo;
    BigDecimal amount;
}
