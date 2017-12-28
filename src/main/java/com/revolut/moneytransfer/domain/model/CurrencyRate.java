package com.revolut.moneytransfer.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

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

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CurrencyRate that = (CurrencyRate) o;
    return Objects.equals(value, that.value);
  }

  @Override public int hashCode()
  {
    return Objects.hash(value);
  }
}
