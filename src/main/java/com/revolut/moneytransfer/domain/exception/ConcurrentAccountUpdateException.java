package com.revolut.moneytransfer.domain.exception;

import static java.lang.String.format;

public class ConcurrentAccountUpdateException extends TransferException
{
  private static final long serialVersionUID = 6390250728449505581L;

  public ConcurrentAccountUpdateException(String accountName)
  {
    super(format("There was an update on %s account meanwhile. Aborting", accountName));
  }
}
