package com.revolut.moneytransfer.api.exception;

import com.revolut.moneytransfer.domain.exception.CustomerNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class CustomerNotFoundMapper implements ExceptionMapper<CustomerNotFoundException>
{
  @Override
  public Response toResponse(CustomerNotFoundException ex)
  {
    return status(404).entity(ex.getMessage()).build();
  }
}
