package com.revolut.moneytransfer;

import com.revolut.moneytransfer.adapter.InMemoryCustomerAccountRepository;
import com.revolut.moneytransfer.adapter.InMemoryExchangeRateRepository;
import com.revolut.moneytransfer.domain.AccountTransferService;
import com.revolut.moneytransfer.domain.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.accepted;

@Path("transfers")
public class TransferController
{
  @POST
  @Consumes(APPLICATION_JSON)
  public Response transfer(TransferRequestDTO dto)
  {
    AccountTransferRequest request = new TransferRequestConverter().convertFrom(dto);
    new AccountTransferService(
        new InMemoryCustomerAccountRepository(
            new HashMap<String, List<Account>>()
            {{
              put("customerId1", new ArrayList<Account>()
              {{
                add(new Account("EUR", TEN, Currency.getInstance("EUR")));
                add(new Account("GBP", ONE, Currency.getInstance("GBP")));
              }});
            }}
        ),
        new InMemoryExchangeRateRepository(new HashMap<String, CurrencyRate>()
        {{
          put("EUR-GBP", new CurrencyRate(new BigDecimal("0.89")));
        }}))
        .execute(request);

    return accepted().build();
  }
}
