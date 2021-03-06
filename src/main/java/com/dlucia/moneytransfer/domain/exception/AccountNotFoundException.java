package com.dlucia.moneytransfer.domain.exception;

import static java.lang.String.format;

public class AccountNotFoundException extends TransferException
{
  private static final long serialVersionUID = -3851372851061781778L;

  public AccountNotFoundException(String accountID)
  {
    super(format("Account with id %s not found", accountID));
  }
}
