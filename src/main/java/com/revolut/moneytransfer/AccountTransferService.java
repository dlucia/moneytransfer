package com.revolut.moneytransfer;

public class AccountTransferService
{

  public void execute(AccountTransferRequest transfer)
  {
    if (transfer.from().equalsIgnoreCase("XXX"))
      throw new AccountNotFoundException(transfer.from());
  }
}
