package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.exception.DatabaseException;
import com.revolut.moneytransfer.domain.model.AccountTransfer;
import org.junit.Test;

import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public abstract class TransferRepositoryContractTest
{
  private static final String CUSTOMER_ID = "anyId";
  private static final AccountTransfer A_TRANSFER = new AccountTransfer(CUSTOMER_ID,
                                                                        "EUR",
                                                                        "USD",
                                                                        ONE,
                                                                        ONE,
                                                                        "");
  private static final AccountTransfer ANOTHER_TRANSFER = new AccountTransfer(CUSTOMER_ID,
                                                                              "GBP",
                                                                              "CHF",
                                                                              ONE,
                                                                              TEN,
                                                                              "hello");

  @Test
  public void save() throws Exception
  {
    repository().save(A_TRANSFER);

    assertThat(transactionsFor(CUSTOMER_ID), contains(A_TRANSFER));
  }

  @Test
  public void manySave() throws Exception
  {
    repository().save(A_TRANSFER);
    repository().save(ANOTHER_TRANSFER);

    assertThat(transactionsFor(CUSTOMER_ID), contains(A_TRANSFER, ANOTHER_TRANSFER));
  }

  @Test(expected = DatabaseException.class)
  public void nullData()
  {
    repository().save(new AccountTransfer(CUSTOMER_ID,
                                          "EUR",
                                          null,
                                          ONE,
                                          ONE,
                                          ""));
  }

  protected abstract TransferRepository repository();

  protected abstract List<AccountTransfer> transactionsFor(String customerId) throws Exception;
}
