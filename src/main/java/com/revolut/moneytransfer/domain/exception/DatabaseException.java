package com.revolut.moneytransfer.domain.exception;

public class DatabaseException extends TransferException
{
  private static final long serialVersionUID = 3834453312365092041L;

  public DatabaseException(String message)
  {
    super(message);
  }
}
