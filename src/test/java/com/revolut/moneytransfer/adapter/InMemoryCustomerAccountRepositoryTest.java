package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.CustomerNotFoundException;
import com.revolut.moneytransfer.domain.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
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
  private static final Account EUR_ACCOUNT = new Account("EUR", of(new BigDecimal("14.15"), "EUR"));
  private static final Account CHF_ACCOUNT = new Account("CHF", of(ONE, "CHF"));
  private static final Account NOT_EXISTENT_ACCOUNT = anAccount().withName("XXX").build();
  private static final String CUSTOMER_ID = "cc1";
  private static final String NOT_EXISTTENT_CUSTOMER_ID = "xxx";

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
    assertThat(repository.lookup(CUSTOMER_ID, "EUR"), is(EUR_ACCOUNT));
  }

  @Test(expected = AccountNotFoundException.class)
  public void accountNotFound()
  {
    repository.lookup(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT.name());
  }

  @Test(expected = CustomerNotFoundException.class)
  public void customerNotFound()
  {
    repository.lookup(NOT_EXISTTENT_CUSTOMER_ID, "EUR");
  }

  @Test
  public void updateAccount()
  {
    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(EUR_ACCOUNT, CHF_ACCOUNT));

    Account account = new Account("EUR", of(new BigDecimal("4.55"), "EUR"));
    repository.updateAccount(CUSTOMER_ID, account);
    assertThat(storage.get(CUSTOMER_ID), containsInAnyOrder(CHF_ACCOUNT, account));
  }

  @Test(expected = AccountNotFoundException.class)
  public void updateNotExistentAccount()
  {
    repository.updateAccount(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT);
  }

}