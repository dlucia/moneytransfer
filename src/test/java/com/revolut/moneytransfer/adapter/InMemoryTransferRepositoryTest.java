package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
import com.revolut.moneytransfer.domain.repository.IdGenerator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InMemoryTransferRepositoryTest
{
  private static final AccountTransferRequest ACCOUNT_TRANSFER = new AccountTransferRequest("aaa",
                                                                                            "USD",
                                                                                            "GBP",
                                                                                            ONE,
                                                                                            "");

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  private IdGenerator idGenerator;

  private InMemoryTransferRepository repository;
  private Map<String, AccountTransferRequest> storage;

  @Before
  public void setUp()
  {
    storage = new HashMap<>();
    repository = new InMemoryTransferRepository(idGenerator, storage);
  }

  @Test
  public void save()
  {
    context.checking(new Expectations()
    {{
      allowing(idGenerator).get();
      will(returnValue("id"));
    }});
    repository.save(ACCOUNT_TRANSFER);

    assertThat(storage.size(), is(1));
    assertThat(storage.get("id"), is(ACCOUNT_TRANSFER));
  }
}