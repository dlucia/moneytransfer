package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.model.CurrencyRate;

import java.util.Currency;

public interface ExchangeRateRepository
{
  CurrencyRate rateFor(Currency currencyFrom, Currency currencyTo);
}
