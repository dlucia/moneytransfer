package com.dlucia.moneytransfer.domain.model;

import javax.money.MonetaryAmount;
import java.time.Instant;

import static java.time.Instant.now;
import static org.javamoney.moneta.Money.of;

public class AccountBuilder
{
  private String name = "EUR";
  private MonetaryAmount amount = of(1, "EUR");
  private Instant instant = now();

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

  public AccountBuilder withLastUpdateInstant(Instant instant)
  {
    this.instant = instant;
    return this;
  }

  public Account build()
  {
    return new Account(name, amount, instant);
  }
}
