package com.dlucia.moneytransfer;

import com.dlucia.moneytransfer.adapter.inmemory.*;
import com.dlucia.moneytransfer.adapter.jdbc.*;
import com.dlucia.moneytransfer.api.converter.Converter;
import com.dlucia.moneytransfer.api.converter.TransferRequestConverter;
import com.dlucia.moneytransfer.domain.AccountTransferService;
import com.dlucia.moneytransfer.domain.model.Account;
import com.dlucia.moneytransfer.domain.model.CurrencyRate;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

import static com.dlucia.moneytransfer.DatabaseBuilder.aDatabase;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.Instant.now;
import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;
import static org.javamoney.moneta.Money.of;

public class Application
{
  public static final String BASE_URI = "http://localhost:8080/api/";
  private static final String RESOURCE_PACKAGE = "com.dlucia.moneytransfer.api";

  public static void main(String[] args)
  {
    startServer();
  }

  public static HttpServer startServer()
  {
    return createHttpServer(URI.create(BASE_URI),
                            new ResourceConfig()
                                .register(new Configuration())
                                // .register(new InMemoryConfiguration())
                                .packages(RESOURCE_PACKAGE));
  }

  static class Configuration extends AbstractBinder
  {
    @Override protected void configure()
    {
      DataSource dataSource = aDatabase().withScript("database.sql").build();

      Converter converter = new TransferRequestConverter();
      AccountTransferService transferService = new AccountTransferService(
          new JdbcCustomerAccountRepository(dataSource),
          new JdbcExchangeRateRepository(dataSource),
          new JdbcTransferRepository(dataSource)
      );

      bind(transferService).to(AccountTransferService.class);
      bind(converter).to(Converter.class);
    }
  }

  static class InMemoryConfiguration extends AbstractBinder
  {
    @Override protected void configure()
    {
      Converter converter = new TransferRequestConverter();
      AccountTransferService transferService = new AccountTransferService(
          new InMemoryCustomerAccountRepository(
              new HashMap<String, List<Account>>()
              {{
                put("customer1", new ArrayList<Account>()
                {{
                  add(new Account("EUR", of(TEN, "EUR"), now()));
                  add(new Account("GBP", of(ONE, "GBP"), now()));
                }});
                put("customer2", new ArrayList<Account>()
                {{
                  add(new Account("CHF", of(new BigDecimal("3"), "CHF"), now()));
                }});
              }}
          ),
          new InMemoryExchangeRateRepository(
              new HashMap<String, CurrencyRate>()
              {{
                put("EUR-GBP", new CurrencyRate(new BigDecimal("0.89")));
                put("GBP-EUR", new CurrencyRate(new BigDecimal("1.13")));
              }}
          ),
          new InMemoryTransferRepository(new HashMap<>())
      );

      bind(transferService).to(AccountTransferService.class);
      bind(converter).to(Converter.class);
    }
  }
}



