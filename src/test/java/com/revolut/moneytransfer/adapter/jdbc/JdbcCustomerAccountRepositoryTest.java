package com.revolut.moneytransfer.adapter.jdbc;

import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepositoryContractTest;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.*;

import static com.revolut.moneytransfer.adapter.jdbc.TestDatabaseBuilder.aDatabase;
import static java.time.Instant.now;

public class JdbcCustomerAccountRepositoryTest extends CustomerAccountRepositoryContractTest
{
  private CustomerAccountRepository repository;
  private DataSource dataSource;

  @Before
  public void setUp()
  {
    dataSource = aDatabase().withScript("test-database.sql").build();
    repository = new JdbcCustomerAccountRepository(dataSource);
  }

  @Override protected void simulateAConcurrentUpdateFor(Account account) throws Exception
  {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection
        .prepareStatement("UPDATE ACCOUNT SET LAST_UPDATE=? WHERE CUSTOMER_ID=? AND NAME=?");
    statement.setTimestamp(1, new Timestamp(now().toEpochMilli()));
    statement.setString(2, CUSTOMER_ID);
    statement.setString(3, account.name());

    statement.executeUpdate();
  }

  @Override protected CustomerAccountRepository repository()
  {
    return repository;
  }
}