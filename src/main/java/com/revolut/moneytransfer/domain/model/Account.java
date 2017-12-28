package com.revolut.moneytransfer.domain.model;

import com.revolut.moneytransfer.domain.exception.InsufficientBalanceException;
import com.revolut.moneytransfer.domain.exception.NegativeAmountException;

import javax.money.*;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.money.Monetary.getRounding;
import static org.javamoney.moneta.Money.of;

public class Account
{
  private final String name;
  private final CurrencyUnit currency;
  private MonetaryAmount balance;

  public Account(String name, MonetaryAmount balance)
  {
    this.name = name;
    this.balance = balance;
    this.currency = balance.getCurrency();
  }

  public String name()
  {
    return name;
  }

  public CurrencyUnit currency()
  {
    return currency;
  }

  public MonetaryAmount balance()
  {
    MonetaryOperator roundingOperator = getRounding(currency);
    return balance.with(roundingOperator);
  }

  public void reduceBalanceOf(BigDecimal amount)
  {
    MonetaryAmount moneyAmount = of(amount, currency);
    validate(moneyAmount);

    if (balance.isLessThan(moneyAmount))
      throw new InsufficientBalanceException(balance, moneyAmount);

    balance = balance.subtract(moneyAmount);
  }

  public void increaseBalanceOf(BigDecimal amount)
  {
    MonetaryAmount moneyAmount = of(amount, currency);
    validate(moneyAmount);

    balance = balance.add(moneyAmount);
  }

  private void validate(MonetaryAmount amount)
  {
    if (amount.isNegative())
      throw new NegativeAmountException(amount);
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Account account = (Account) o;
    return Objects.equals(name, account.name) &&
        Objects.equals(currency, account.currency) &&
        Objects.equals(balance, account.balance);
  }

  @Override public int hashCode()
  {

    return Objects.hash(name, currency, balance);
  }
}
