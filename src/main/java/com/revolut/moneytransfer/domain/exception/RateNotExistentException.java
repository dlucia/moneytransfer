package com.revolut.moneytransfer.domain.exception;

public class RateNotExistentException extends TransferException
{
  private static final long serialVersionUID = 4650396066385000309L;

  public RateNotExistentException(String message)
  {
    super(message);
  }
}
