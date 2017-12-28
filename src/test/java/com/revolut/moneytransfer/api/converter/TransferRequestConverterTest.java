package com.revolut.moneytransfer.api.converter;

import com.revolut.moneytransfer.api.TransferRequestDTO;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
import org.junit.Before;
import org.junit.Test;

import static com.revolut.moneytransfer.api.TransferRequestBuilder.aTransferRequest;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferRequestConverterTest
{
  private Converter converter;

  @Before
  public void setUp()
  {
    converter = new TransferRequestConverter();
  }

  @Test
  public void convert()
  {
    TransferRequestDTO request =
        aTransferRequest()
            .withAccountFrom("sender")
            .withAccountTo("receiver")
            .withAmount("10")
            .withNote("some notes")
            .build();

    assertThat(converter.convertFrom(request),
               is(new AccountTransferRequest(request.customerId,
                                             "sender",
                                             "receiver",
                                             TEN,
                                             "some notes")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidAmount()
  {
    TransferRequestDTO request =
        aTransferRequest()
            .withAmount("a")
            .build();

    converter.convertFrom(request);
  }
}