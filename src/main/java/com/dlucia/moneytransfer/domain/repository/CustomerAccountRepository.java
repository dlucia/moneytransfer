package com.dlucia.moneytransfer.domain.repository;

import com.dlucia.moneytransfer.domain.model.Account;

public interface CustomerAccountRepository
{
  Account lookup(String customerId, String account);

  void updateAccountBalanceFor(String customerId,
                               Account fromAccount,
                               Account toAccount);
}
