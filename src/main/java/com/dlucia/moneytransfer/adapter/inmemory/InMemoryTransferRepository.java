package com.dlucia.moneytransfer.adapter.inmemory;

import com.dlucia.moneytransfer.domain.exception.DatabaseException;
import com.dlucia.moneytransfer.domain.model.AccountTransfer;
import com.dlucia.moneytransfer.domain.repository.TransferRepository;

import java.util.Map;
import java.util.UUID;

public class InMemoryTransferRepository implements TransferRepository
{
  private final Map<String, AccountTransfer> storage;

  public InMemoryTransferRepository(Map<String, AccountTransfer> storage)
  {
    this.storage = storage;
  }

  @Override public void save(AccountTransfer transfer)
  {
    String id = UUID.randomUUID().toString();
    validate(transfer);
    storage.put(id, transfer);
  }

  private void validate(AccountTransfer transfer)
  {
    if (transfer.customerId() == null)
      throw new DatabaseException("CustomerId field can not be null");
    if (transfer.from() == null)
      throw new DatabaseException("accountFrom field can not be null");
    if (transfer.to() == null)
      throw new DatabaseException("accountTo field can not be null");
    if (transfer.amount() == null)
      throw new DatabaseException("amount field can not be null");
    if (transfer.exchangeRate() == null)
      throw new DatabaseException("exchangeRate field can not be null");
  }
}
