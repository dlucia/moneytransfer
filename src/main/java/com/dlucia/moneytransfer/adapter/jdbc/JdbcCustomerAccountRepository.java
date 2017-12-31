package com.dlucia.moneytransfer.adapter.jdbc;

import com.dlucia.moneytransfer.domain.exception.*;
import com.dlucia.moneytransfer.domain.model.Account;
import com.dlucia.moneytransfer.domain.repository.CustomerAccountRepository;
import org.javamoney.moneta.Money;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

public class JdbcCustomerAccountRepository implements CustomerAccountRepository
{
  private final DataSource dataSource;

  public JdbcCustomerAccountRepository(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public Account lookup(String customerId, String account)
  {
    Connection connection = null;
    try
    {
      connection = dataSource.getConnection();
      PreparedStatement statement = connection
          .prepareStatement("SELECT * FROM ACCOUNT WHERE CUSTOMER_ID=? AND NAME=?");
      statement.setString(1, customerId);
      statement.setString(2, account);
      ResultSet resultSet = statement.executeQuery();

      List<Account> accounts = mapToAccounts(resultSet);
      return accounts
          .stream()
          .findFirst()
          .orElseThrow(() -> new AccountNotFoundException(account));
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

  private List<Account> mapToAccounts(ResultSet resultSet) throws SQLException
  {
    List<Account> accounts = new ArrayList<>();
    while (resultSet.next())
    {
      String name = resultSet.getString("NAME");
      String balance = resultSet.getString("BALANCE");
      String currency = resultSet.getString("CURRENCY");
      Timestamp lastUpdate = resultSet.getTimestamp("LAST_UPDATE");

      accounts.add(new Account(name, Money.of(new BigDecimal(balance), currency), lastUpdate.toInstant()));
    }
    return accounts;
  }

  @Override public void updateAccountBalanceFor(String customerId,
                                                Account fromAccount,
                                                Account toAccount)
  {
    Connection connection = null;
    try
    {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);

      checkForConcurrentUpdateOn(customerId, fromAccount);
      updateAccount(customerId, fromAccount, connection);

      checkForConcurrentUpdateOn(customerId, toAccount);
      updateAccount(customerId, toAccount, connection);

      connection.commit();
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

  private void checkForConcurrentUpdateOn(String customerId, Account toStore)
  {
    Account stored = lookup(customerId, toStore.name());
    if (stored.lastUpdateInstant().isAfter(toStore.lastUpdateInstant()))
      throw new ConcurrentAccountUpdateException(stored.name());
  }

  private void updateAccount(String customerId, Account account, Connection connection) throws SQLException
  {
    PreparedStatement statement = connection
        .prepareStatement("UPDATE ACCOUNT SET BALANCE=?, LAST_UPDATE=? WHERE CUSTOMER_ID=? AND NAME=?");
    statement.setString(1, account.balance().getNumber().toString());
    statement.setTimestamp(2, new Timestamp(now().toEpochMilli()));
    statement.setString(3, customerId);
    statement.setString(4, account.name());

    int rowAffected = statement.executeUpdate();
    if (rowAffected == 0)
      throw new AccountNotFoundException(account.name());
  }

  private void closeConnection(Connection connection)
  {
    try
    {
      connection.close();
    }
    catch (SQLException ignored)
    {
    }
  }
}
