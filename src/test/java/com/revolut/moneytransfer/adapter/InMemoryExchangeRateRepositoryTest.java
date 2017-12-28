package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.exception.RateNotExistentException;
import com.revolut.moneytransfer.domain.model.CurrencyRate;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InMemoryExchangeRateRepositoryTest
{
  private static final CurrencyRate EUR_CHF_RATE = new CurrencyRate(new BigDecimal("1.2"));
  private static final CurrencyRate GBP_EUR_RATE = new CurrencyRate(new BigDecimal("1.4"));
  private static final CurrencyUnit EUR = CurrencyUnitBuilder.of("EUR", "").build();
  private static final CurrencyUnit CHF = CurrencyUnitBuilder.of("CHF", "").build();
  private static final CurrencyUnit GBP = CurrencyUnitBuilder.of("GBP", "").build();

  private InMemoryExchangeRateRepository repository;
  private Map<String, CurrencyRate> storage;

  @Before
  public void setUp()
  {
    storage = new HashMap<String, CurrencyRate>()
    {{
      put("EUR-CHF", EUR_CHF_RATE);
      put("GBP-EUR", GBP_EUR_RATE);
    }};

    repository = new InMemoryExchangeRateRepository(storage);
  }

  @Test
  public void rateFor()
  {
    assertThat(repository.rateFor(EUR, CHF), is(EUR_CHF_RATE));
  }

  @Test(expected = RateNotExistentException.class)
  public void notExistentRate()
  {
    repository.rateFor(EUR, GBP);
  }
}