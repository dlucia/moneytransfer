package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.RateNotExistentException;
import com.revolut.moneytransfer.domain.model.CurrencyRate;
import com.revolut.moneytransfer.domain.repository.ExchangeRateRepository;

import java.util.Currency;
import java.util.Map;

public class InMemoryExchangeRateRepository implements ExchangeRateRepository
{
  private final Map<String, CurrencyRate> storage;

  public InMemoryExchangeRateRepository(Map<String, CurrencyRate> storage)
  {
    this.storage = storage;
  }

  @Override public CurrencyRate rateFor(Currency currencyFrom, Currency currencyTo)
  {
    String key = currencyFrom.getCurrencyCode() + "-" + currencyTo.getCurrencyCode();

    CurrencyRate currencyRate = storage.get(key);
    if (currencyRate == null)
      throw new RateNotExistentException(key);

    return currencyRate;
  }
}
