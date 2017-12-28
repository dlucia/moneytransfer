package com.revolut.moneytransfer;

public interface Converter
{
  AccountTransferRequest convertFrom(TransferRequestDTO dto);
}
