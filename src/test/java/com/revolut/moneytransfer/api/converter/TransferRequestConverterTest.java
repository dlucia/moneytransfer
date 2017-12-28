package com.revolut.moneytransfer.api.converter;

import com.revolut.moneytransfer.api.TransferRequestDTO;
import com.revolut.moneytransfer.api.converter.Converter;
import com.revolut.moneytransfer.api.converter.TransferRequestConverter;
import com.revolut.moneytransfer.domain.model.AccountTransferRequest;
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
    TransferRequestDTO request = new TransferRequestDTO("customerId",
                                                        "sender",
                                                        "receiver",
                                                        "10",
                                                        "some notes");

    assertThat(converter.convertFrom(request),
               is(new AccountTransferRequest("customerId",
                                             "sender",
                                             "receiver",
                                             TEN,
                                             "some notes")));
  }

}