package com.dlucia.moneytransfer.domain.repository;

import com.dlucia.moneytransfer.domain.exception.AccountNotFoundException;
import com.dlucia.moneytransfer.domain.exception.ConcurrentAccountUpdateException;
import com.dlucia.moneytransfer.domain.model.Account;
import org.junit.Test;

import java.math.BigDecimal;

import static com.dlucia.moneytransfer.domain.model.AccountBuilder.anAccount;
import static java.math.BigDecimal.ONE;
import static java.time.Instant.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.javamoney.moneta.Money.of;
import static org.junit.Assert.assertThat;

public abstract class CustomerAccountRepositoryContractTest
{
  protected static final String CUSTOMER_ID = "cc1";
  protected static final Account EUR_ACCOUNT =
      anAccount()
          .withName("EUR")
          .withBalance(of(new BigDecimal("14.15"), "EUR"))
          .withLastUpdateInstant(parse("2017-12-31T10:49:34.873Z"))
          .build();
  protected static final Account GBP_ACCOUNT =
      anAccount()
          .withName("GBP")
          .withBalance(of(ONE, "GBP"))
          .withLastUpdateInstant(parse("2017-12-13T09:40:34.873Z"))
          .build();
  private static final Account NOT_EXISTENT_ACCOUNT = anAccount().withName("XXX").build();

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
    Account eurUpdatedAccount =
        anAccount()
            .withName("EUR")
            .withBalance(of(new BigDecimal("4.55"), "EUR"))
            .withLastUpdateInstant(parse("2017-12-31T10:49:34.873Z"))
            .build();
    Account gbpUpdatedAccount =
        anAccount()
            .withName("GBP")
            .withBalance(of(new BigDecimal("0.55"), "GBP"))
            .withLastUpdateInstant(parse("2017-12-13T09:40:34.873Z"))
            .build();

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
    Account eurUpdatedAccount =
        anAccount()
            .withName("EUR")
            .withBalance(of(new BigDecimal("4.55"), "EUR"))
            .withLastUpdateInstant(parse("2017-12-31T10:49:34.873Z"))
            .build();
    Account gbpUpdatedAccount =
        anAccount()
            .withName("GBP")
            .withBalance(of(new BigDecimal("0.55"), "GBP"))
            .withLastUpdateInstant(parse("2017-12-13T09:40:34.873Z"))
            .build();

    simulateAConcurrentUpdateFor(EUR_ACCOUNT);
    repository().updateAccountBalanceFor(CUSTOMER_ID, eurUpdatedAccount, gbpUpdatedAccount);
  }

  @Test
  public void concurrentBalanceAccountsUpdateDoesNotModifyStorage() throws Exception
  {
    Account eurUpdatedAccount =
        anAccount()
            .withName("EUR")
            .withBalance(of(new BigDecimal("4.55"), "EUR"))
            .withLastUpdateInstant(parse("2017-12-31T10:49:34.873Z"))
            .build();
    Account gbpUpdatedAccount =
        anAccount()
            .withName("GBP")
            .withBalance(of(new BigDecimal("0.55"), "GBP"))
            .withLastUpdateInstant(parse("2017-12-13T09:40:34.873Z"))
            .build();

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
