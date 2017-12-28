package com.revolut.moneytransfer.domain.exception;

import static java.lang.String.format;

public class RateNotExistentException extends TransferException
{
  private static final long serialVersionUID = 4650396066385000309L;

  public RateNotExistentException(String message)
  {
    super(format("Does not exists exchange rate for the pair %s", message));
  }
}
