package com.dws.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Entity(name = "DWS_ACCOUNT")
@Getter
@Setter
public class AccountEntity {

    @Id
    private Long id;

    private BigDecimal balance;

    // Constructors, getters, and setters
}
