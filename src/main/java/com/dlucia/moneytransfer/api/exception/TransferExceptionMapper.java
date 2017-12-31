package com.dlucia.moneytransfer.api.exception;

import com.dlucia.moneytransfer.domain.exception.TransferException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class TransferExceptionMapper implements ExceptionMapper<TransferException>
{
  @Override
  public Response toResponse(TransferException ex)
  {
    return status(500).entity(ex.getMessage()).build();
  }

}
