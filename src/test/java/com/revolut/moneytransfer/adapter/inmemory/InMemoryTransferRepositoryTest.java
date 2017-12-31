package com.revolut.moneytransfer.adapter.inmemory;

import com.revolut.moneytransfer.domain.model.AccountTransfer;
import com.revolut.moneytransfer.domain.repository.TransferRepository;
import com.revolut.moneytransfer.domain.repository.TransferRepositoryContractTest;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class InMemoryTransferRepositoryTest extends TransferRepositoryContractTest
{
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private InMemoryTransferRepository repository;
  private Map<String, AccountTransfer> storage;

  @Before
  public void setUp()
  {
    storage = new HashMap<>();
    repository = new InMemoryTransferRepository(storage);
  }

  @Override protected TransferRepository repository()
  {
    return repository;
  }

  @Override protected List<AccountTransfer> transactionsFor(String customerId) throws Exception
  {
    return storage.values()
        .stream()
        .filter(t -> t.customerId().equalsIgnoreCase(customerId))
        .collect(toList());
  }
}