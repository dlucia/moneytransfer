package com.revolut.moneytransfer;

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
