package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;

public class AccountTransferService
{

  private final CustomerAccountRepository customerAccountRepository;

  public AccountTransferService(CustomerAccountRepository customerAccountRepository)
  {
    this.customerAccountRepository = customerAccountRepository;
  }

  public void execute(AccountTransferRequest transfer)
  {
    Account from = customerAccountRepository.lookup(transfer.customerId(), transfer.from());
  }
}
