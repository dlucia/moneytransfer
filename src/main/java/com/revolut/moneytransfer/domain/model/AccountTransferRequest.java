package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountTransferRequest
{
  private final String customerId;
  private final String from;
  private final String to;
  private final BigDecimal amount;
  private final String note;

  public AccountTransferRequest(String customerId, String from, String to, BigDecimal amount, String note)
  {
    this.customerId = customerId;
    this.from = from;
    this.to = to;
    this.amount = amount;
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

  public String getNote()
  {
    return note;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AccountTransferRequest that = (AccountTransferRequest) o;
    return Objects.equals(customerId, that.customerId) &&
        Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(amount, that.amount) &&
        Objects.equals(note, that.note);
  }

  @Override public int hashCode()
  {
    return Objects.hash(customerId, from, to, amount, note);
  }

  @Override public String toString()
  {
    return "AccountTransferRequest{" +
        "customerId='" + customerId + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", amount=" + amount +
        ", note='" + note + '\'' +
        '}';
  }
}
