package com.revolut.moneytransfer.domain.repository;

import com.revolut.moneytransfer.domain.exception.RateNotExistentException;
import com.revolut.moneytransfer.domain.model.CurrencyRate;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.junit.Test;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class ExchangeRateRepositoryContractTest
{
  protected static final CurrencyRate EUR_CHF_RATE = new CurrencyRate(new BigDecimal("1.2"));
  protected static final CurrencyRate GBP_EUR_RATE = new CurrencyRate(new BigDecimal("1.4"));
  private static final CurrencyUnit EUR = CurrencyUnitBuilder.of("EUR", "").build();
  private static final CurrencyUnit CHF = CurrencyUnitBuilder.of("CHF", "").build();
  private static final CurrencyUnit GBP = CurrencyUnitBuilder.of("GBP", "").build();

  @Test
  public void rateFor()
  {
    assertThat(repository().rateFor(EUR, CHF), is(EUR_CHF_RATE));
  }

  @Test(expected = RateNotExistentException.class)
  public void notExistentRate()
  {
    repository().rateFor(EUR, GBP);
  }

  @Test
  public void sameCurrency()
  {
    assertThat(repository().rateFor(EUR, EUR), is(new CurrencyRate(ONE)));
  }

  protected abstract ExchangeRateRepository repository();
}
