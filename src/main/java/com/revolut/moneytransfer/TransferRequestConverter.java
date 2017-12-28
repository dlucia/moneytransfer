package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.AccountTransferRequest;

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
