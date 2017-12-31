package com.dlucia.moneytransfer.api.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class AnyExceptionMapper implements ExceptionMapper<Exception>
{
  @Override
  public Response toResponse(Exception ex)
  {
    return status(500).entity(ex.getMessage()).build();
  }
}
