package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountTransfer
{
  private final String customerId;
  private final String from;
  private final String to;
  private final BigDecimal amount;
  private final BigDecimal exchangeRate;
  private final String note;

  public AccountTransfer(String customerId,
                         String from,
                         String to,
                         BigDecimal amount,
                         BigDecimal exchangeRate,
                         String note)
  {
    this.customerId = customerId;
    this.from = from;
    this.to = to;
    this.amount = amount;
    this.exchangeRate = exchangeRate;
    this.note = note;
  }

  public String customerId()
  {
    return customerId;
  }

  public String from()
  {
    return from;
  }

  public String to()
  {
    return to;
  }

  public BigDecimal amount()
  {
    return amount;
  }

  public BigDecimal exchangeRate()
  {
    return exchangeRate;
  }

  public String note()
  {
    return note;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AccountTransfer that = (AccountTransfer) o;
    return Objects.equals(customerId, that.customerId) &&
        Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(amount, that.amount) &&
        Objects.equals(exchangeRate, that.exchangeRate) &&
        Objects.equals(note, that.note);
  }

  @Override public int hashCode()
  {
    return Objects.hash(customerId, from, to, amount, exchangeRate, note);
  }

  @Override public String toString()
  {
    return "AccountTransfer{" +
        "customerId='" + customerId + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", amount=" + amount +
        ", exchangeRate=" + exchangeRate +
        ", note='" + note + '\'' +
        '}';
  }
}
