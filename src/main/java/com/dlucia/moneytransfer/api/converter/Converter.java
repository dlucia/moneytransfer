package com.dlucia.moneytransfer.api.converter;

import com.dlucia.moneytransfer.api.TransferRequestDTO;
import com.dlucia.moneytransfer.domain.model.AccountTransferRequest;

public interface Converter
{
  AccountTransferRequest convertFrom(String customerId, TransferRequestDTO dto);
}
