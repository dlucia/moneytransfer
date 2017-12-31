package com.dlucia.moneytransfer.domain.repository;

import com.dlucia.moneytransfer.domain.model.AccountTransfer;

public interface TransferRepository
{
  void save(AccountTransfer transfer);
}
