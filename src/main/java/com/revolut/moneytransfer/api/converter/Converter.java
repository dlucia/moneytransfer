package com.revolut.moneytransfer.api.converter;

import com.revolut.moneytransfer.api.TransferRequestDTO;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;

public interface Converter
{
  AccountTransferRequest convertFrom(TransferRequestDTO dto);
}
