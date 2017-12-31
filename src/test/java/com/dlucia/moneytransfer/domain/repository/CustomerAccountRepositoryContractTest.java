package com.dlucia.moneytransfer.domain.repository;

import com.dlucia.moneytransfer.domain.exception.AccountNotFoundException;
import com.dlucia.moneytransfer.domain.exception.ConcurrentAccountUpdateException;
import com.dlucia.moneytransfer.domain.model.Account;
import com.dlucia.moneytransfer.domain.model.AccountBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.javamoney.moneta.Money.of;
import static org.junit.Assert.assertThat;

public abstract class CustomerAccountRepositoryContractTest
{
  protected static final String CUSTOMER_ID = "cc1";
  protected static final Account EUR_ACCOUNT = new Account("EUR",
                                                           of(new BigDecimal("14.15"), "EUR"),
                                                           parse("2017-12-31T10:49:34.873Z"));
  protected static final Account GBP_ACCOUNT = new Account("GBP",
                                                           of(ONE, "GBP"),
                                                           parse("2017-12-13T09:40:34.873Z"));
  private static final Account NOT_EXISTENT_ACCOUNT = AccountBuilder.anAccount().withName("XXX").build();

  @Test
  public void found()
  {
    assertThat(repository().lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));
  }

  @Test(expected = AccountNotFoundException.class)
  public void accountNotFound()
  {
    repository().lookup(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT.name());
  }

  @Test
  public void balanceAccountUpdate()
  {
    Account eurUpdatedAccount = new Account(EUR_ACCOUNT.name(),
                                            of(new BigDecimal("4.55"), EUR_ACCOUNT.name()),
                                            EUR_ACCOUNT.lastUpdateInstant());
    Account gbpUpdatedAccount = new Account(GBP_ACCOUNT.name(),
                                            of(new BigDecimal("0.55"), GBP_ACCOUNT.name()),
                                            EUR_ACCOUNT.lastUpdateInstant());

    repository().updateAccountBalanceFor(CUSTOMER_ID, eurUpdatedAccount, gbpUpdatedAccount);

    assertThat(repository().lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(eurUpdatedAccount));
  }

  @Test(expected = AccountNotFoundException.class)
  public void updateNotExistentAccount()
  {
    repository().updateAccountBalanceFor(CUSTOMER_ID, NOT_EXISTENT_ACCOUNT, EUR_ACCOUNT);
  }

  @Test(expected = ConcurrentAccountUpdateException.class)
  public void concurrentBalanceAccountsUpdate() throws Exception
  {
    Account eurUpdatedAccount = new Account(EUR_ACCOUNT.name(),
                                            of(new BigDecimal("4.55"), EUR_ACCOUNT.name()),
                                            EUR_ACCOUNT.lastUpdateInstant());
    Account gbpUpdatedAccount = new Account(GBP_ACCOUNT.name(),
                                            of(new BigDecimal("0.55"), GBP_ACCOUNT.name()),
                                            GBP_ACCOUNT.lastUpdateInstant());

    simulateAConcurrentUpdateFor(EUR_ACCOUNT);
    repository().updateAccountBalanceFor(CUSTOMER_ID, eurUpdatedAccount, gbpUpdatedAccount);
  }

  @Test
  public void concurrentBalanceAccountsUpdateDoesNotModifyStorage() throws Exception
  {
    Account eurUpdatedAccount = new Account(EUR_ACCOUNT.name(),
                                            of(new BigDecimal("4.55"), EUR_ACCOUNT.name()),
                                            EUR_ACCOUNT.lastUpdateInstant());
    Account gbpUpdatedAccount = new Account(GBP_ACCOUNT.name(),
                                            of(new BigDecimal("0.55"), GBP_ACCOUNT.name()),
                                            GBP_ACCOUNT.lastUpdateInstant());

    simulateAConcurrentUpdateFor(GBP_ACCOUNT);
    try
    {
      repository().updateAccountBalanceFor(CUSTOMER_ID, eurUpdatedAccount, gbpUpdatedAccount);
    }
    catch (Exception ignored)
    {
    }

    assertThat(repository().lookup(CUSTOMER_ID, EUR_ACCOUNT.name()), is(EUR_ACCOUNT));
    assertThat(repository().lookup(CUSTOMER_ID, GBP_ACCOUNT.name()), is(GBP_ACCOUNT));
  }

  protected abstract CustomerAccountRepository repository();

  protected abstract void simulateAConcurrentUpdateFor(Account account) throws Exception;
}
