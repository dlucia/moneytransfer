package com.dlucia.moneytransfer.api.converter;

import com.dlucia.moneytransfer.api.TransferRequestDTO;
import com.dlucia.moneytransfer.domain.model.AccountTransferRequest;

import java.math.BigDecimal;

public class TransferRequestConverter implements Converter
{
  @Override public AccountTransferRequest convertFrom(String customerId, TransferRequestDTO dto)
  {
    return new AccountTransferRequest(customerId,
                                      dto.accountFrom,
                                      dto.accountTo,
                                      new BigDecimal(dto.amount),
                                      dto.note);
  }
}
