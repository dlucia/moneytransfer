package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.model.Account;

public interface CustomerAccountRepository
{
  Account lookup(String customerId, String account);

  void updateAccount(String customerId, Account account);
}
