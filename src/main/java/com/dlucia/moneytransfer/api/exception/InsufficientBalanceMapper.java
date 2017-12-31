package com.dlucia.moneytransfer.api.exception;

import com.dlucia.moneytransfer.domain.exception.InsufficientBalanceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class InsufficientBalanceMapper implements ExceptionMapper<InsufficientBalanceException>
{
  @Override
  public Response toResponse(InsufficientBalanceException ex)
  {
    return status(400).entity(ex.getMessage()).build();
  }
}
