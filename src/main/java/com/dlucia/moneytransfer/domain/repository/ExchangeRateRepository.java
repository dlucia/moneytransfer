package com.dlucia.moneytransfer.domain.repository;

import com.dlucia.moneytransfer.domain.model.CurrencyRate;

import javax.money.CurrencyUnit;

public interface ExchangeRateRepository
{
  CurrencyRate rateFor(CurrencyUnit currencyFrom, CurrencyUnit currencyTo);
}
