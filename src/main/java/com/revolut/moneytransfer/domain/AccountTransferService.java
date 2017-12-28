package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.domain.model.*;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;
import com.revolut.moneytransfer.domain.repository.ExchangeRateRepository;

public class AccountTransferService
{

  private final CustomerAccountRepository customerAccountRepository;
  private final ExchangeRateRepository exchangeRateRepository;

  public AccountTransferService(CustomerAccountRepository customerAccountRepository,
                                ExchangeRateRepository exchangeRateRepository)
  {
    this.customerAccountRepository = customerAccountRepository;
    this.exchangeRateRepository = exchangeRateRepository;
  }

  public void execute(AccountTransferRequest transfer)
  {
    Account from = customerAccountRepository.lookup(transfer.customerId(), transfer.from());
    Account to = customerAccountRepository.lookup(transfer.customerId(), transfer.to());

    CurrencyRate currencyRate = exchangeRateRepository.rateFor(from.currency(), to.currency());
    from.reduceBalanceOf(transfer.amount());
    to.increaseBalanceOf(transfer.amount().multiply(currencyRate.value()));

    customerAccountRepository.updateAccount(transfer.customerId(), from);
    customerAccountRepository.updateAccount(transfer.customerId(), to);
  }
}