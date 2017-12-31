package com.revolut.moneytransfer.adapter.jdbc;

import com.revolut.moneytransfer.domain.exception.DatabaseException;
import com.revolut.moneytransfer.domain.model.AccountTransfer;
import com.revolut.moneytransfer.domain.repository.TransferRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;

public class JdbcTransferRepository implements TransferRepository
{
  private final DataSource dataSource;

  public JdbcTransferRepository(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public void save(AccountTransfer transfer)
  {
    Connection connection = null;
    try
    {
      connection = dataSource.getConnection();
      PreparedStatement statement = connection
          .prepareStatement(
              "INSERT INTO TRANSFER "
                  + "(CUSTOMER_ID,ACCOUNT_FROM,ACCOUNT_TO,AMOUNT,EXCHANGE_RATE,NOTE,CREATION_DATE)"
                  + " VALUES (?,?,?,?,?,?,?)");
      statement.setString(1, transfer.customerId());
      statement.setString(2, transfer.from());
      statement.setString(3, transfer.to());
      statement.setString(4, transfer.amount().toString());
      statement.setString(5, transfer.exchangeRate().toString());
      statement.setString(6, transfer.note());
      statement.setTimestamp(7, new Timestamp(Instant.now().toEpochMilli()));

      statement.execute();
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
    finally
    {
      closeConnection(connection);
    }
  }

  private void closeConnection(Connection connection)
  {
    try
    {
      connection.close();
    }
    catch (SQLException ignored) {}
  }
}
