package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.model.AccountTransferRequest;

public interface TransferRepository
{
  void save(AccountTransferRequest transfer);
}
