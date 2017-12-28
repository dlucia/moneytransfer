package com.revolut.moneytransfer.api;

import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class AccountNotFoundMapper implements ExceptionMapper<AccountNotFoundException>
{
  @Override
  public Response toResponse(AccountNotFoundException ex)
  {
    return status(404).entity(ex.getMessage()).build();
  }
}
