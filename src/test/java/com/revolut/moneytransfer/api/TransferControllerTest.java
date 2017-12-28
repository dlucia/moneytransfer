package com.revolut.moneytransfer.api;

import com.revolut.moneytransfer.Application;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.revolut.moneytransfer.api.TransferRequestBuilder.aTransferRequest;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferControllerTest
{
  private static final String TRANSFER_PATH = "api/v1/transfers";

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp()
  {
    server = Application.startServer();
    target = newClient().target(Application.BASE_URI);
  }

  @After
  public void tearDown()
  {
    server.shutdown();
  }

  @Test
  public void transferAccepted()
  {
    Response response =
        target.path(TRANSFER_PATH)
            .request()
            .post(json(aTransferRequest()
                           .withCustomerID("customerId1")
                           .withAccountFrom("EUR")
                           .withAccountTo("GBP")
                           .build()));

    assertThat(response.getStatus(), is(202));
  }

  @Test
  public void accountNotFound()
  {
    Response response =
        target.path(TRANSFER_PATH)
            .request()
            .post(json(aTransferRequest()
                           .withCustomerID("customerId1")
                           .withAccountFrom("notExistent")
                           .build()));

    assertThat(response.getStatus(), is(404));
    assertThat(response.readEntity(String.class), is("Account with id notExistent not found"));
  }
}
