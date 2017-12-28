package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.domain.model.*;
import com.revolut.moneytransfer.domain.repository.*;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.*;

import java.math.BigDecimal;
import java.util.Currency;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

public class AccountTransferServiceTest
{
  private static final Account EUR_ACCOUNT = new Account("EUR", TEN, Currency.getInstance("EUR"));
  private static final Account CHF_ACCOUNT = new Account("CHF", ONE, Currency.getInstance("CHF"));
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
      will(returnValue(EUR_ACCOUNT));
      allowing(customerAccountRepository).lookup(CUSTOMER_ID, transferRequest.to());
      will(returnValue(CHF_ACCOUNT));

      allowing(exchangeRateRepository).rateFor(EUR_ACCOUNT.currency(), CHF_ACCOUNT.currency());
      will(returnValue(EUR_CHF_RATE));

      oneOf(customerAccountRepository)
          .updateAccount(CUSTOMER_ID, new Account("EUR", new BigDecimal("9"), Currency.getInstance("EUR")));
      oneOf(customerAccountRepository)
          .updateAccount(CUSTOMER_ID, new Account("CHF", new BigDecimal("2.5"), Currency.getInstance("CHF")));

      oneOf(transferRepository).save(transferRequest);
    }});

    transferService.execute(transferRequest);
  }
}