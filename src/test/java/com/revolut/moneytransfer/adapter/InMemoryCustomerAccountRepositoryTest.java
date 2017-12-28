package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;
import com.revolut.moneytransfer.domain.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InMemoryCustomerAccountRepositoryTest
{
  private static final Account EUR_ACCOUNT = new Account("EUR",
                                                         new BigDecimal("14.15"),
                                                         Currency.getInstance("EUR"));
  private static final Account CHF_ACCOUNT = new Account("CHF", ONE, Currency.getInstance("CHF"));
  private static final Account NOT_EXISTENT_ACCOUNT = new Account("XXX", ZERO, null);

  private InMemoryCustomerAccountRepository repository;
  private Map<String, List<Account>> storage;

  @Before
  public void setUp()
  {
    storage = new HashMap<String, List<Account>>()
    {{
      put("cc1", new ArrayList<Account>()
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
    assertThat(repository.lookup("cc1", "EUR"), is(EUR_ACCOUNT));
  }

  @Test(expected = AccountNotFoundException.class)
  public void notFound()
  {
    repository.lookup("cc1", NOT_EXISTENT_ACCOUNT.name());
  }

}