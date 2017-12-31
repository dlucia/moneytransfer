package com.revolut.moneytransfer.adapter.inmemory;

import com.revolut.moneytransfer.domain.exception.RateNotExistentException;
import com.revolut.moneytransfer.domain.model.CurrencyRate;
import com.revolut.moneytransfer.domain.repository.ExchangeRateRepository;

import javax.money.CurrencyUnit;
import java.util.Map;

import static java.math.BigDecimal.ONE;

public class InMemoryExchangeRateRepository implements ExchangeRateRepository
{
  private final Map<String, CurrencyRate> storage;

  public InMemoryExchangeRateRepository(Map<String, CurrencyRate> storage)
  {
    this.storage = storage;
  }

  @Override public CurrencyRate rateFor(CurrencyUnit currencyFrom, CurrencyUnit currencyTo)
  {
    if (currencyFrom.equals(currencyTo))
      return new CurrencyRate(ONE);

    String key = key(currencyFrom, currencyTo);
    CurrencyRate currencyRate = storage.get(key);
    if (currencyRate == null)
      throw new RateNotExistentException(key);

    return currencyRate;
  }

  private String key(CurrencyUnit currencyFrom, CurrencyUnit currencyTo)
  {
    return currencyFrom.getCurrencyCode() + "-" + currencyTo.getCurrencyCode();
  }
}
