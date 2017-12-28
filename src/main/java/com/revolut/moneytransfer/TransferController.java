package com.revolut.moneytransfer;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.accepted;

@Path("transfers")
public class TransferController
{
  @POST
  @Consumes(APPLICATION_JSON)
  public Response transfer(TransferRequestDTO dto)
  {
    return accepted().build();
  }
}
