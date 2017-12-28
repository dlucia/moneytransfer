package com.revolut.moneytransfer.domain.model;

import com.revolut.moneytransfer.domain.exception.InsufficientBalanceException;
import com.revolut.moneytransfer.domain.exception.NegativeAmountException;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.moneytransfer.domain.model.AccountBuilder.anAccount;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.CoreMatchers.is;
import static org.javamoney.moneta.Money.of;
import static org.junit.Assert.assertThat;

public class AccountTest
{
  @Test
  public void reduceBalance()
  {
    Account account = anAccount().withBalance(of(TEN, "EUR")).build();
    account.reduceBalanceOf(ONE);

    assertThat(account.balance(), is(of(9, "EUR")));
  }

  @Test(expected = InsufficientBalanceException.class)
  public void insufficientBalance()
  {
    Account account = anAccount().withBalance(of(TEN, "EUR")).build();
    account.reduceBalanceOf(new BigDecimal("10.1"));
  }

  @Test
  public void increaseBalance()
  {
    Account account = anAccount().withBalance(of(new BigDecimal("4.45"), "EUR")).build();
    account.increaseBalanceOf(new BigDecimal("10.55"));

    assertThat(account.balance(), is(of(new BigDecimal("15.00"), "EUR")));
  }

  @Test(expected = NegativeAmountException.class)
  public void increaseBalanceWithNegativeAmount()
  {
    Account account = anAccount().withBalance(of(ONE, "EUR")).build();
    account.increaseBalanceOf(new BigDecimal("-10"));
  }

  @Test(expected = NegativeAmountException.class)
  public void reduceBalanceWithNegativeAmount()
  {
    Account account = anAccount().build();
    account.reduceBalanceOf(new BigDecimal("-10"));
  }

  @Test
  public void roundingCurrencyBased()
  {
    Account account = anAccount().withBalance(of(new BigDecimal("12.415143423431423"), "EUR")).build();
    assertThat(account.balance(), is(of(new BigDecimal("12.42"), "EUR")));

    account = anAccount().withBalance(of(new BigDecimal("12.415143423431423"), "JPY")).build();
    assertThat(account.balance(), is(of(new BigDecimal("12"), "JPY")));
  }
}