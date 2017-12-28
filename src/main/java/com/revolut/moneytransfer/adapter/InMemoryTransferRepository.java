package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
import com.revolut.moneytransfer.domain.repository.IdGenerator;
import com.revolut.moneytransfer.domain.repository.TransferRepository;

import java.util.Map;

public class InMemoryTransferRepository implements TransferRepository
{
  private final IdGenerator idGenerator;
  private final Map<String, AccountTransferRequest> storage;

  public InMemoryTransferRepository(IdGenerator idGenerator,
                                    Map<String, AccountTransferRequest> storage)
  {
    this.idGenerator = idGenerator;
    this.storage = storage;
  }

  @Override public void save(AccountTransferRequest transfer)
  {
    String id = idGenerator.get();

    storage.put(id, transfer);
  }
}
