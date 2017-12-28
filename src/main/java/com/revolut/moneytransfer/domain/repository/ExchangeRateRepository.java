package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.model.CurrencyRate;

import javax.money.CurrencyUnit;

public interface ExchangeRateRepository
{
  CurrencyRate rateFor(CurrencyUnit currencyFrom, CurrencyUnit currencyTo);
}
