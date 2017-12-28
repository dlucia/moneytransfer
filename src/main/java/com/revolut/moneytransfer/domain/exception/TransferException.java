package com.revolut.moneytransfer.domain.exception;

public class TransferException extends RuntimeException
{
  private static final long serialVersionUID = -7273770739113985941L;

  public TransferException(String message)
  {
    super(message);
  }
}
