package com.revolut.moneytransfer.adapter;

import com.revolut.moneytransfer.domain.repository.IdGenerator;

import java.util.UUID;

public class UUIDIdGenerator implements IdGenerator
{
  @Override public String get()
  {
    return UUID.randomUUID().toString();
  }
}
