package com.dlucia.moneytransfer.domain.exception;

import static java.lang.String.format;

public class CustomerNotFoundException extends TransferException
{
  private static final long serialVersionUID = -6203267349600554558L;

  public CustomerNotFoundException(String customerId)
  {
    super(format("Customer with id %s not found", customerId));
  }
}
