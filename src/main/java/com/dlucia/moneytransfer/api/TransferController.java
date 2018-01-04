package com.dlucia.moneytransfer.api;

import com.dlucia.moneytransfer.api.converter.Converter;
import com.dlucia.moneytransfer.domain.AccountTransferService;
import com.dlucia.moneytransfer.domain.model.AccountTransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.accepted;

@Path("v1/customers")
public class TransferController
{
  private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

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
  @Path("/{customerId}/transfers")
  @Consumes(APPLICATION_JSON)
  public Response transfer(@PathParam("customerId") String customerId, TransferRequestDTO dto)
  {
    logger.info("Transfer request {} from {}", dto, customerId);
    AccountTransferRequest transfer = converter.convertFrom(customerId, dto);
    transferService.execute(transfer);

    return accepted().build();
  }
}
