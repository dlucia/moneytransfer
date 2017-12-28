package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.CustomerNotFoundException;
import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;

import java.util.List;
import java.util.Map;

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
    if (accounts == null)
      throw new CustomerNotFoundException(customerId);

    return accounts
        .stream()
        .filter(a -> a.name().equalsIgnoreCase(accountID))
        .findFirst()
        .orElseThrow(() -> new AccountNotFoundException(accountID));
  }

  @Override public void updateAccount(String customerId, Account updated)
  {
    Account previous = lookup(customerId, updated.name());
    List<Account> accounts = storage.get(customerId);
    accounts.remove(previous);
    accounts.add(updated);

    storage.put(customerId, accounts);
  }
}
