package com.revolut.moneytransfer;

import java.util.Objects;

public class TransferRequestDTO
{
  public String customerId;
  public String accountFrom;
  public String accountTo;
  public String amount;
  public String note;

  public TransferRequestDTO() {}

  public TransferRequestDTO(String customerId,
                            String accountFrom,
                            String accountTo,
                            String amount,
                            String note)
  {
    this.customerId = customerId;
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.amount = amount;
    this.note = note;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TransferRequestDTO that = (TransferRequestDTO) o;
    return Objects.equals(customerId, that.customerId) &&
        Objects.equals(accountFrom, that.accountFrom) &&
        Objects.equals(accountTo, that.accountTo) &&
        Objects.equals(amount, that.amount) &&
        Objects.equals(note, that.note);
  }

  @Override public int hashCode()
  {
    return Objects.hash(customerId, accountFrom, accountTo, amount, note);
  }

  @Override public String toString()
  {
    return "TransferRequestDTO{" +
        "customerId='" + customerId + '\'' +
        ", from='" + accountFrom + '\'' +
        ", to='" + accountTo + '\'' +
        ", amount='" + amount + '\'' +
        ", note='" + note + '\'' +
        '}';
  }
}
