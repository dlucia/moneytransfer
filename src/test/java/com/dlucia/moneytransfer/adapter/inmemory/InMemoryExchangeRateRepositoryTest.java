package com.dlucia.moneytransfer.adapter.inmemory;

import com.dlucia.moneytransfer.domain.model.CurrencyRate;
import com.dlucia.moneytransfer.domain.repository.ExchangeRateRepository;
import com.dlucia.moneytransfer.domain.repository.ExchangeRateRepositoryContractTest;
import org.junit.Before;

import java.util.HashMap;

public class InMemoryExchangeRateRepositoryTest extends ExchangeRateRepositoryContractTest
{
  private InMemoryExchangeRateRepository repository;

  @Before
  public void setUp()
  {
    repository = new InMemoryExchangeRateRepository(
        new HashMap<String, CurrencyRate>()
        {{
          put("EUR-CHF", EUR_CHF_RATE);
          put("GBP-EUR", GBP_EUR_RATE);
        }}
    );
  }

  @Override protected ExchangeRateRepository repository()
  {
    return repository;
  }
}