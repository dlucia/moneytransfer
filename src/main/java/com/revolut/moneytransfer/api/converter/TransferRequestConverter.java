package com.revolut.moneytransfer.api.converter;

import com.revolut.moneytransfer.api.TransferRequestDTO;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;

import java.math.BigDecimal;

public class TransferRequestConverter implements Converter
{
  @Override public AccountTransferRequest convertFrom(TransferRequestDTO dto)
  {
    return new AccountTransferRequest(dto.customerId,
                                      dto.accountFrom,
                                      dto.accountTo,
                                      new BigDecimal(dto.amount),
                                      dto.note);
  }
}
