package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Account
{
  private final String name;
  private BigDecimal balance;
  private final Currency currency;

  public Account(String name, BigDecimal balance, Currency currency)
  {
    this.name = name;
    this.balance = balance;
    this.currency = currency;
  }

  public String name()
  {
    return name;
  }

  public Currency currency()
  {
    return currency;
  }

  public void reduceBalanceOf(BigDecimal amount)
  {
    this.balance = this.balance.subtract(amount);
  }

  public void increaseBalanceOf(BigDecimal amount)
  {
    this.balance = this.balance.add(amount);
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Account account = (Account) o;
    return Objects.equals(name, account.name) &&
        Objects.equals(balance, account.balance) &&
        Objects.equals(currency, account.currency);
  }

  @Override public int hashCode()
  {

    return Objects.hash(name, balance, currency);
  }
}
