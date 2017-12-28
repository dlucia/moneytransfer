package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.domain.model.Account;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
import com.revolut.moneytransfer.domain.repository.CustomerAccountRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.*;

import java.util.Currency;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

public class AccountTransferServiceTest
{
  private static final Account EUR_ACCOUNT = new Account("EUR", TEN, Currency.getInstance("EUR"));
  private static final Account CHF_ACCOUNT = new Account("CHF", ONE, Currency.getInstance("CHF"));
  private static final String CUSTOMER_ID = "aaa";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  private CustomerAccountRepository customerAccountRepository;

  private AccountTransferService transferService;

  @Before
  public void setUp()
  {
    transferService = new AccountTransferService(customerAccountRepository);
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
    }});

    transferService.execute(transferRequest);
  }
}