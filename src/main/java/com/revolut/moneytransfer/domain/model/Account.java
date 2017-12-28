package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;
import java.util.Currency;

public class Account
{
  private final String name;
  private final BigDecimal balance;
  private final Currency currency;

  public Account(String name, BigDecimal balance, Currency currency)
  {
    this.name = name;
    this.balance = balance;
    this.currency = currency;
  }
}
