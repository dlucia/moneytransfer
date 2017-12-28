package com.revolut.moneytransfer.api;

import com.revolut.moneytransfer.api.converter.Converter;
import com.revolut.moneytransfer.domain.AccountTransferService;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.accepted;

@Path("api/v1/transfers")
public class TransferController
{
  private final Converter converter;
  private final AccountTransferService transferService;

  @Inject
  public TransferController(Converter converter,
                            AccountTransferService transferService)
  {
    this.converter = converter;
    this.transferService = transferService;
  }

  @POST
  @Consumes(APPLICATION_JSON)
  public Response transfer(TransferRequestDTO dto)
  {
    AccountTransferRequest transfer = converter.convertFrom(dto);
    transferService.execute(transfer);

    return accepted().build();
  }
}
