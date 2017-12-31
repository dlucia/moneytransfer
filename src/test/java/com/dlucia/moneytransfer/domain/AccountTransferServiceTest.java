package com.dlucia.moneytransfer.domain;

import com.dlucia.moneytransfer.domain.exception.ConcurrentAccountUpdateException;
import com.dlucia.moneytransfer.domain.exception.InsufficientBalanceException;
import com.dlucia.moneytransfer.domain.model.*;
import com.dlucia.moneytransfer.domain.repository.*;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.*;

import java.math.BigDecimal;

import static com.dlucia.moneytransfer.domain.model.AccountBuilder.anAccount;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.javamoney.moneta.Money.of;

public class AccountTransferServiceTest
{
  private Account eurAccount;
  private Account chfAccount;
  private static final CurrencyRate EUR_CHF_RATE = new CurrencyRate(new BigDecimal("1.5"));
  private static final String CUSTOMER_ID = "aaa";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  private CustomerAccountRepository customerAccountRepository;
  @Mock
  private ExchangeRateRepository exchangeRateRepository;
  @Mock
  private TransferRepository transferRepository;

  private AccountTransferService transferService;

  @Before
  public void setUp()
  {
    eurAccount = anAccount()
        .withName("EUR")
        .withBalance(of(TEN, "EUR"))
        .build();
    chfAccount = anAccount()
        .withName("CHF")
        .withBalance(of(ONE, "CHF"))
        .build();
    transferService = new AccountTransferService(customerAccountRepository,
                                                 exchangeRateRepository,
                                                 transferRepository);
  }

  @Test
  public void execute()
  {
    AccountTransferRequest transferRequest = new AccountTransferRequest(CUSTOMER_ID, "EUR", "CHF", ONE, "");

    context.checking(new Expectations()
    {{
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transferRequest.from());
      will(returnValue(eurAccount));
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transferRequest.to());
      will(returnValue(chfAccount));

      allowing(exchangeRateRepository).rateFor(eurAccount.currency(), chfAccount.currency());
      will(returnValue(EUR_CHF_RATE));

      oneOf(customerAccountRepository).updateAccountBalanceFor(CUSTOMER_ID,
                                                               anAccount()
                                                                   .withName("EUR")
                                                                   .withBalance(of(new BigDecimal("9"),
                                                                                   "EUR"))
                                                                   .build(),
                                                               anAccount()
                                                                   .withName("CHF")
                                                                   .withBalance(of(new BigDecimal("2.5"),
                                                                                   "CHF"))
                                                                   .build());

      oneOf(transferRepository).save(new AccountTransfer(CUSTOMER_ID,
                                                         transferRequest.from(),
                                                         transferRequest.to(),
                                                         transferRequest.amount(),
                                                         EUR_CHF_RATE.value(), ""));
    }});

    transferService.execute(transferRequest);
  }

  @Test(expected = InsufficientBalanceException.class)
  public void insufficientBalance()
  {
    AccountTransferRequest transfer = new AccountTransferRequest(CUSTOMER_ID,
                                                                 "EUR",
                                                                 "CHF",
                                                                 new BigDecimal("11"),
                                                                 "");

    context.checking(new Expectations()
    {{
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transfer.from());
      will(returnValue(eurAccount));
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transfer.to());
      will(returnValue(chfAccount));

      allowing(exchangeRateRepository).rateFor(eurAccount.currency(), chfAccount.currency());
      will(returnValue(EUR_CHF_RATE));
    }});

    transferService.execute(transfer);
  }

  @Test(expected = ConcurrentAccountUpdateException.class)
  public void concurrentAccountUpdate()
  {
    AccountTransferRequest transfer = new AccountTransferRequest(CUSTOMER_ID,
                                                                 "EUR",
                                                                 "CHF",
                                                                 new BigDecimal("8"),
                                                                 "");

    context.checking(new Expectations()
    {{
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transfer.from());
      will(returnValue(eurAccount));
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transfer.to());
      will(returnValue(chfAccount));

      allowing(exchangeRateRepository).rateFor(eurAccount.currency(), chfAccount.currency());
      will(returnValue(EUR_CHF_RATE));

      oneOf(customerAccountRepository).updateAccountBalanceFor(CUSTOMER_ID,
                                                               anAccount()
                                                                   .withName("EUR")
                                                                   .withBalance(of(new BigDecimal("2"),
                                                                                   "EUR"))
                                                                   .build(),
                                                               anAccount()
                                                                   .withName("CHF")
                                                                   .withBalance(of(new BigDecimal("13"),
                                                                                   "CHF"))
                                                                   .build());

      will(throwException(new ConcurrentAccountUpdateException("")));
    }});

    transferService.execute(transfer);
  }
}