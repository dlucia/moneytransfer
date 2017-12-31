package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.*;
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

  @Override public void updateAccountBalanceFor(String customerId, Account toSave)
  {
    Account current = lookup(customerId, toSave.name());
    List<Account> accounts = storage.get(customerId);

    checkForConcurrentUpdate(toSave, current);

    toSave.updateLastUpdateInstant();
    accounts.remove(current);
    accounts.add(toSave);

    storage.put(customerId, accounts);
  }

  private void checkForConcurrentUpdate(Account toSave, Account current)
  {
    if (current.lastUpdateInstant().isAfter(toSave.lastUpdateInstant()))
      throw new ConcurrentAccountUpdateException(toSave.name());
  }
}
