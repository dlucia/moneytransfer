package com.revolut.moneytransfer.adapter.jdbc;

import com.revolut.moneytransfer.domain.exception.DatabaseException;
import com.revolut.moneytransfer.domain.exception.RateNotExistentException;
import com.revolut.moneytransfer.domain.model.CurrencyRate;
import com.revolut.moneytransfer.domain.repository.ExchangeRateRepository;

import javax.money.CurrencyUnit;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExchangeRateRepository implements ExchangeRateRepository
{
  private final DataSource dataSource;

  public JdbcExchangeRateRepository(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public CurrencyRate rateFor(CurrencyUnit currencyFrom, CurrencyUnit currencyTo)
  {
    Connection connection = null;
    try
    {
      connection = dataSource.getConnection();
      PreparedStatement statement = connection
          .prepareStatement("SELECT * FROM EXCHANGE_RATE WHERE KEY=?");
      statement.setString(1, key(currencyFrom, currencyTo));

      ResultSet resultSet = statement.executeQuery();
      List<CurrencyRate> currencyRates = mapToCurrencyRates(resultSet);
      return currencyRates.stream()
          .findFirst()
          .orElseThrow(() -> new RateNotExistentException(key(currencyFrom, currencyTo)));
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

  private String key(CurrencyUnit currencyFrom, CurrencyUnit currencyTo)
  {
    return currencyFrom.getCurrencyCode() + "-" + currencyTo.getCurrencyCode();
  }

  private List<CurrencyRate> mapToCurrencyRates(ResultSet resultSet) throws SQLException
  {
    List<CurrencyRate> accounts = new ArrayList<>();
    while (resultSet.next())
    {
      BigDecimal value = resultSet.getBigDecimal("VALUE");

      accounts.add(new CurrencyRate(value));
    }
    return accounts;
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
