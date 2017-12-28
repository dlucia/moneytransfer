package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;

public class CurrencyRate
{
  private BigDecimal value;

  public CurrencyRate(BigDecimal value)
  {
    this.value = value;
  }

  public BigDecimal value()
  {
    return value;
  }
}
