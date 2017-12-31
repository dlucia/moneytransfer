package com.revolut.moneytransfer.adapter.jdbc;

import com.revolut.moneytransfer.domain.model.AccountTransfer;
import com.revolut.moneytransfer.domain.repository.TransferRepository;
import com.revolut.moneytransfer.domain.repository.TransferRepositoryContractTest;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.revolut.moneytransfer.adapter.jdbc.TestDatabaseBuilder.aDatabase;

public class JdbcTransferRepositoryTest extends TransferRepositoryContractTest
{

  private TransferRepository repository;
  private DataSource dataSource;

  @Before
  public void setUp()
  {
    dataSource = aDatabase().withScript("test-database.sql").build();
    repository = new JdbcTransferRepository(dataSource);
  }

  @Override protected TransferRepository repository()
  {
    return repository;
  }

  protected List<AccountTransfer> transactionsFor(String customerId) throws Exception
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