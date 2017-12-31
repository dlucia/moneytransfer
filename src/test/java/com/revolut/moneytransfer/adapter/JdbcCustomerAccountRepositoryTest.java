package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.ConcurrentAccountUpdateException;
import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;
import org.junit.*;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static com.revolut.moneytransfer.adapter.TestDatabaseBuilder.aDatabase;
import static com.revolut.moneytransfer.domain.model.AccountBuilder.anAccount;
import static java.time.Instant.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.javamoney.moneta.Money.of;
import static org.junit.Assert.assertThat;

public class JdbcCustomerAccountRepositoryTest
{
  private static final String CUSTOMER_ID = "cc1";
  private static final Account EUR_ACCOUNT = new Account("EUR",
                                                         of(new BigDecimal("14.15"), "EUR"),
                                                         parse("2017-12-31T10:49:34.873Z"));
  private static final Account NOT_EXISTENT_ACCOUNT = anAccount().withName("XXX").build();

  private CustomerAccountRepository repository;

  @Before
  public void setUp()
  {
    DataSource dataSource = aDatabase().withScript("test-database.sql").build();
    repository = new JdbcCustomerAccountRepository(dataSource);
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

  @Test
  public void balanceAccountUpdate()
  {
    assertThat(repository.lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));

    Account updatedAccount = new Account(EUR_ACCOUNT.name(),
                                         of(new BigDecimal("4.55"), EUR_ACCOUNT.name()),
                                         EUR_ACCOUNT.lastUpdateInstant());
    repository.updateAccountBalanceFor(CUSTOMER_ID, updatedAccount);

    assertThat(repository.lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(updatedAccount));
  }

  @Test(expected = ConcurrentAccountUpdateException.class)
  @Ignore
  public void concurrentBalanceAccountUpdate()
  {
    assertThat(repository.lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));

    simulateAConcurrentUpdateFor(EUR_ACCOUNT);
    repository.updateAccountBalanceFor(CUSTOMER_ID, EUR_ACCOUNT);

    assertThat(repository.lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));
  }

  private void simulateAConcurrentUpdateFor(Account eurAccount)
  {
  }

  @Test(expected = AccountNotFoundException.class)
  public void updateNotExistentAccount()
  {
    repository.updateAccountBalanceFor(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT);
  }
}