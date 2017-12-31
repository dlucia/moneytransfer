package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.domain.model.*;
import com.revolut.moneytransfer.domain.repository.*;

import java.math.BigDecimal;

public class AccountTransferService
{
  private final CustomerAccountRepository customerAccountRepository;
  private final ExchangeRateRepository exchangeRateRepository;
  private final TransferRepository transferRepository;

  public AccountTransferService(CustomerAccountRepository customerAccountRepository,
                                ExchangeRateRepository exchangeRateRepository,
                                TransferRepository transferRepository)
  {
    this.customerAccountRepository = customerAccountRepository;
    this.exchangeRateRepository = exchangeRateRepository;
    this.transferRepository = transferRepository;
  }

  public void execute(AccountTransferRequest transfer)
  {
    Account from = customerAccountRepository.lookup(transfer.customerId(), transfer.from());
    Account to = customerAccountRepository.lookup(transfer.customerId(), transfer.to());

    CurrencyRate currencyRate = exchangeRateRepository.rateFor(from.currency(), to.currency());

    updateBalance(transfer.customerId(), from, to, transfer.amount(), currencyRate);

    transferRepository.save(new AccountTransfer(transfer.customerId(),
                                                transfer.from(),
                                                transfer.to(),
                                                transfer.amount(),
                                                currencyRate.value(),
                                                transfer.getNote()));
  }

  private void updateBalance(String customerId, Account fromAccount, Account toAccount,
                             BigDecimal amount, CurrencyRate currencyRate)
  {
    fromAccount.reduceBalanceOf(amount);
    toAccount.increaseBalanceOf(amount.multiply(currencyRate.value()));

    customerAccountRepository.updateAccountBalanceFor(customerId, fromAccount,toAccount);
  }
}
