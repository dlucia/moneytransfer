package com.dlucia.moneytransfer.api.exception;

import com.dlucia.moneytransfer.domain.exception.ConcurrentAccountUpdateException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class ConcurrentAccountUpdateMapper implements ExceptionMapper<ConcurrentAccountUpdateException>
{
  @Override
  public Response toResponse(ConcurrentAccountUpdateException ex)
  {
    return status(503).entity(ex.getMessage()).build();
  }
}
