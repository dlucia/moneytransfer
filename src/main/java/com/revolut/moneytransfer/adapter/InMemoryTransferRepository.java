package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.model.AccountTransfer;
import com.revolut.moneytransfer.domain.repository.IdGenerator;
import com.revolut.moneytransfer.domain.repository.TransferRepository;

import java.util.Map;

public class InMemoryTransferRepository implements TransferRepository
{
  private final IdGenerator idGenerator;
  private final Map<String, AccountTransfer> storage;

  public InMemoryTransferRepository(IdGenerator idGenerator,
                                    Map<String, AccountTransfer> storage)
  {
    this.idGenerator = idGenerator;
    this.storage = storage;
  }

  @Override public void save(AccountTransfer transfer)
  {
    String id = idGenerator.get();

    storage.put(id, transfer);
  }
}
