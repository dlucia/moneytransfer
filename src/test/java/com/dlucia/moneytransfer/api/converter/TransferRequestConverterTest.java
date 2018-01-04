package com.dlucia.moneytransfer.api.converter;

import com.dlucia.moneytransfer.api.TransferRequestBuilder;
import com.dlucia.moneytransfer.api.TransferRequestDTO;
import com.dlucia.moneytransfer.domain.model.AccountTransferRequest;
import org.junit.Before;
import org.junit.Test;

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
        TransferRequestBuilder.aTransferRequest()
            .withAccountFrom("sender")
            .withAccountTo("receiver")
            .withAmount("10")
            .withNote("some notes")
            .build();

    assertThat(converter.convertFrom("customerId", request),
               is(new AccountTransferRequest("customerId",
                                             "sender",
                                             "receiver",
                                             TEN,
                                             "some notes")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidAmount()
  {
    TransferRequestDTO request =
        TransferRequestBuilder.aTransferRequest()
            .withAmount("a")
            .build();

    converter.convertFrom("customerId", request);
  }
}