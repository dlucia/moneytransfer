package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.model.AccountTransferRequest;

public interface Converter
{
  AccountTransferRequest convertFrom(TransferRequestDTO dto);
}
