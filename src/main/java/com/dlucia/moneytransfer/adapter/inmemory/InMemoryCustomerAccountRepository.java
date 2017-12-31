package com.dlucia.moneytransfer.adapter.inmemory;

import com.dlucia.moneytransfer.domain.exception.*;
import com.dlucia.moneytransfer.domain.model.Account;
import com.dlucia.moneytransfer.domain.repository.CustomerAccountRepository;

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

  @Override public void updateAccountBalanceFor(String customerId, Account fromAccount, Account toAccount)
  {
    checkForConcurrentUpdateOn(customerId, fromAccount);
    checkForConcurrentUpdateOn(customerId, toAccount);

    updateAccount(customerId, fromAccount);
    updateAccount(customerId, toAccount);
  }

  private void checkForConcurrentUpdateOn(String customerId, Account toStore)
  {
    Account stored = lookup(customerId, toStore.name());
    if (stored.lastUpdateInstant().isAfter(toStore.lastUpdateInstant()))
      throw new ConcurrentAccountUpdateException(stored.name());
  }

  private void updateAccount(String customerId, Account updated)
  {
    Account previous = lookup(customerId, updated.name());
    List<Account> accounts = storage.get(customerId);
    accounts.remove(previous);
    accounts.add(updated);

    storage.put(customerId, accounts);
  }
}
