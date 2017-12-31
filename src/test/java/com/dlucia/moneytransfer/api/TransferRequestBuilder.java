package com.dlucia.moneytransfer.api;

public class TransferRequestBuilder
{
  private String customerId = "ccc";
  private String accountFrom = "xxx";
  private String accountTo = "yyy";
  private String amount = "1";
  private String note;

  private TransferRequestBuilder() {}

  public static TransferRequestBuilder aTransferRequest()
  {
    return new TransferRequestBuilder();
  }

  public TransferRequestBuilder withCustomerID(String customerId)
  {
    this.customerId = customerId;
    return this;
  }

  public TransferRequestBuilder withAccountFrom(String accountFrom)
  {
    this.accountFrom = accountFrom;
    return this;
  }

  public TransferRequestBuilder withAccountTo(String accountTo)
  {
    this.accountTo = accountTo;
    return this;
  }

  public TransferRequestBuilder withAmount(String amount)
  {
    this.amount = amount;
    return this;
  }

  public TransferRequestBuilder withNote(String note)
  {
    this.note = note;
    return this;
  }

  public TransferRequestDTO build()
  {
    return new TransferRequestDTO(customerId, accountFrom, accountTo, amount, note);
  }
}
