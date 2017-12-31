package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.*;
import com.revolut.moneytransfer.domain.model.Account;
import org.junit.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static com.revolut.moneytransfer.domain.model.AccountBuilder.anAccount;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.javamoney.moneta.Money.of;
import static org.junit.Assert.assertThat;

public class InMemoryCustomerAccountRepositoryTest
{
  private static final Account EUR_ACCOUNT = new Account("EUR", of(new BigDecimal("14.15"), "EUR"),
                                                         Instant.now());
  private static final Account CHF_ACCOUNT = new Account("CHF", of(ONE, "CHF"), Instant.now());
  private static final Account NOT_EXISTENT_ACCOUNT = anAccount().withName("XXX").build();
  private static final String CUSTOMER_ID = "cc1";
  private static final String NOT_EXISTENT_CUSTOMER_ID = "xxx";

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
        add(CHF_ACCOUNT);
      }});
      put("cc2", singletonList(CHF_ACCOUNT));
    }};

    repository = new InMemoryCustomerAccountRepository(storage);
  }

  @Test
  public void found()
  {
    assertThat(repository.lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));
  }

  @Test(expected = AccountNotFoundException.class)
  public void accountNotFound()
  {
    repository.lookup(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT.name());
  }

  @Test(expected = CustomerNotFoundException.class)
  public void customerNotFound()
  {
    repository.lookup(NOT_EXISTENT_CUSTOMER_ID, EUR_ACCOUNT.name());
  }

  @Test
  public void balanceAccountUpdate()
  {
    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(EUR_ACCOUNT, CHF_ACCOUNT));

    Account account = new Account(EUR_ACCOUNT.name(), of(new BigDecimal("4.55"), EUR_ACCOUNT.name()),
                                  Instant.now());
    repository.updateAccountBalanceFor(CUSTOMER_ID, account);

    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(CHF_ACCOUNT, account));
  }

  @Test(expected = ConcurrentAccountUpdateException.class)
  public void concurrentBalanceAccountUpdate()
  {
    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(EUR_ACCOUNT, CHF_ACCOUNT));

    simulateAConcurrentUpdateFor(EUR_ACCOUNT);
    repository.updateAccountBalanceFor(CUSTOMER_ID, EUR_ACCOUNT);

    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(CHF_ACCOUNT, EUR_ACCOUNT));
  }

  private void simulateAConcurrentUpdateFor(Account eurAccount)
  {
    storage.put(CUSTOMER_ID, new ArrayList<Account>()
    {{
      add(new Account("EUR", of(new BigDecimal("14.15"), "EUR"), Instant.now()));
      add(CHF_ACCOUNT);
    }});
  }

  @Test(expected = AccountNotFoundException.class)
  public void updateNotExistentAccount()
  {
    repository.updateAccountBalanceFor(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT);
  }

}