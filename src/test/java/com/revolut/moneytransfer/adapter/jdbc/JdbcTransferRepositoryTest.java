package com.revolut.moneytransfer.adapter.jdbc;

import com.revolut.moneytransfer.domain.exception.DatabaseException;
import com.revolut.moneytransfer.domain.model.AccountTransfer;
import com.revolut.moneytransfer.domain.repository.TransferRepository;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.revolut.moneytransfer.adapter.jdbc.TestDatabaseBuilder.aDatabase;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class JdbcTransferRepositoryTest
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

  private TransferRepository repository;
  private DataSource dataSource;

  @Before
  public void setUp()
  {
    dataSource = aDatabase().withScript("test-database.sql").build();
    repository = new JdbcTransferRepository(dataSource);
  }

  @Test
  public void save() throws Exception
  {
    repository.save(A_TRANSFER);

    assertThat(transactionsFor(CUSTOMER_ID), contains(A_TRANSFER));
  }

  @Test
  public void manySave() throws Exception
  {
    repository.save(A_TRANSFER);
    repository.save(ANOTHER_TRANSFER);

    assertThat(transactionsFor(CUSTOMER_ID), contains(A_TRANSFER, ANOTHER_TRANSFER));
  }

  @Test(expected = DatabaseException.class)
  public void nullData()
  {
    repository.save(new AccountTransfer(CUSTOMER_ID,
                                        "EUR",
                                        null,
                                        ONE,
                                        ONE,
                                        ""));
  }

  private List<AccountTransfer> transactionsFor(String customerId) throws Exception
  {
    try (Connection connection = dataSource.getConnection())
    {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM TRANSFER WHERE CUSTOMER_ID=?");
      preparedStatement.setString(1, customerId);
      ResultSet resultSet = preparedStatement.executeQuery();

      List<AccountTransfer> transfers = new ArrayList<>();
      while (resultSet.next())
      {
        transfers.add(new AccountTransfer(resultSet.getString("CUSTOMER_ID"),
                                          resultSet.getString("ACCOUNT_FROM"),
                                          resultSet.getString("ACCOUNT_TO"),
                                          resultSet.getBigDecimal("AMOUNT"),
                                          resultSet.getBigDecimal("EXCHANGE_RATE"),
                                          resultSet.getString("NOTE")));
      }
      return transfers;
    }
  }
}