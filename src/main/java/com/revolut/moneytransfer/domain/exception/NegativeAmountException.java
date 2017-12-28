package com.revolut.moneytransfer.domain.exception;

import javax.money.MonetaryAmount;

import static java.lang.String.format;

public class NegativeAmountException extends TransferException
{
  private static final long serialVersionUID = -8328617151013021366L;

  public NegativeAmountException(MonetaryAmount amount)
  {
    super(format("Invalid value %s. It has to be positive", amount.toString()));
  }
}
