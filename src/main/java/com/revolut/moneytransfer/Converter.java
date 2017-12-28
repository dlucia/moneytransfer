package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.AccountTransferRequest;

public interface Converter
{
  AccountTransferRequest convertFrom(TransferRequestDTO dto);
}
