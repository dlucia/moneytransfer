package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.model.AccountTransfer;

public interface TransferRepository
{
  void save(AccountTransfer transfer);
}
