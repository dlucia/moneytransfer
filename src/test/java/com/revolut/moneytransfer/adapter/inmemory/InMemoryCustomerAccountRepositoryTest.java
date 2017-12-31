package com.revolut.moneytransfer.adapter.inmemory;

import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepositoryContractTest;
import org.junit.Before;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.parse;
import static org.javamoney.moneta.Money.of;

public class InMemoryCustomerAccountRepositoryTest extends CustomerAccountRepositoryContractTest
{
  private InMemoryCustomerAccountRepository repository;
  private Map<String, List<Account>> storage;

  @Before
  public void setUp()
  {
    storage = new HashMap<String, List<Account>>()
    {{
      put(CUSTOMER_ID, new ArrayList<Account>()
      {{
        add(EUR_ACCOUNT);
        add(GBP_ACCOUNT);
      }});
    }};
    repository = new InMemoryCustomerAccountRepository(storage);
  }

  @Override protected CustomerAccountRepository repository()
  {
    return repository;
  }

  @Override protected void simulateAConcurrentUpdateFor(Account account) throws Exception
  {
    Account eurAccount = EUR_ACCOUNT;
    if (account.name().equalsIgnoreCase("EUR"))
      eurAccount = new Account("EUR",
                               of(new BigDecimal("14.15"), "EUR"),
                               parse("2017-12-31T15:49:34.873Z"));

    Account gbpAccount = GBP_ACCOUNT;
    if (account.name().equalsIgnoreCase("GBP"))
      gbpAccount = new Account("GBP",
                               of(ONE, "GBP"),
                               parse("2017-12-13T19:40:34.873Z"));
    List<Account> accounts = new ArrayList<>();
    accounts.add(eurAccount);
    accounts.add(gbpAccount);

    storage.put(CUSTOMER_ID, accounts);
  }
}