package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.AccountNotFoundException;
import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;

import java.util.*;

public class InMemoryCustomerAccountRepository implements CustomerAccountRepository
{
  private final Map<String, List<Account>> storage;

  public InMemoryCustomerAccountRepository(Map<String, List<Account>> storage)
  {
    this.storage = storage;
  }

  @Override public Account lookup(String customerId, String accountID)
  {
    List<Account> accounts = storage.get(customerId);

    return accounts
        .stream()
        .filter(a -> a.name().equalsIgnoreCase(accountID))
        .findFirst()
        .orElseThrow(() -> new AccountNotFoundException(accountID));
  }

  @Override public void updateAccount(String customerId, Account account)
  {

  }
}
