package com.dlucia.moneytransfer.adapter.jdbc;

import com.dlucia.moneytransfer.domain.model.AccountTransfer;
import com.dlucia.moneytransfer.domain.repository.TransferRepository;
import com.dlucia.moneytransfer.domain.repository.TransferRepositoryContractTest;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferRepositoryTest extends TransferRepositoryContractTest
{
  private TransferRepository repository;
  private DataSource dataSource;

  @Before
  public void setUp()
  {
    dataSource = TestDatabaseBuilder.aDatabase().withScript("test-database.sql").build();
    repository = new JdbcTransferRepository(dataSource);
  }

  @Override protected TransferRepository repository()
  {
    return repository;
  }

  @Override protected List<AccountTransfer> transactionsFor(String customerId) throws Exception
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