package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.AccountNotFoundException;
import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;

public class InMemoryCustomerAccountRepository implements CustomerAccountRepository
{
  @Override public Account lookup(String customerId, String account)
  {
    if (account.equalsIgnoreCase("XXX"))
      throw new AccountNotFoundException(account);
    return null;
  }
}
