package com.revolut.moneytransfer.api;

import com.revolut.moneytransfer.Application;
import com.revolut.moneytransfer.api.TransferRequestDTO;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferControllerTest
{
  private static final String TRANSFER_PATH = "transfers";

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
            .post(json(new TransferRequestDTO("customerId1", "EUR", "GBP", "1", "")));

    assertThat(response.getStatus(), is(202));
  }

  @Test
  public void accountNotFound()
  {
    Response response =
        target.path(TRANSFER_PATH)
            .request()
            .post(json(new TransferRequestDTO("customerId1", "XXX", "GBP", "1", "")));

    assertThat(response.getStatus(), is(404));
  }
}