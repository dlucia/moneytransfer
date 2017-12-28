package com.revolut.moneytransfer.domain.exception;

import javax.money.MonetaryAmount;

import static java.lang.String.format;

public class InsufficientBalanceException extends TransferException
{
  private static final long serialVersionUID = 8484355798918889876L;

  public InsufficientBalanceException(MonetaryAmount amount, MonetaryAmount amountToReduce)
  {
    super(format("Balance insufficient (%s) for a reduce of %s", amount.toString(), amountToReduce.toString()));
  }
}
