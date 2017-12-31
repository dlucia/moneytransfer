package com.revolut.moneytransfer.domain.model;

import javax.money.MonetaryAmount;

import java.time.Instant;

import static org.javamoney.moneta.Money.of;

public class AccountBuilder
{
  private String name = "EUR";
  private MonetaryAmount amount = of(1, "EUR");

  private AccountBuilder() {}

  public static AccountBuilder anAccount()
  {
    return new AccountBuilder();
  }

  public AccountBuilder withName(String name)
  {
    this.name = name;
    return this;
  }

  public AccountBuilder withBalance(MonetaryAmount amount)
  {
    this.amount = amount;
    return this;
  }

  public Account build()
  {
    return new Account(name, amount, Instant.now());
  }
}
