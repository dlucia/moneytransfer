package com.revolut.moneytransfer.adapter;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class UUIDIdGeneratorTest
{
  private UUIDIdGenerator uuidIdGenerator;

  @Before
  public void setUp()
  {
    uuidIdGenerator = new UUIDIdGenerator();
  }

  @Test
  public void getRandomly()
  {
    String id1 = uuidIdGenerator.get();
    String id2 = uuidIdGenerator.get();
    String id3 = uuidIdGenerator.get();

    assertThat(id1.length(), is(36));
    assertThat(id2.length(), is(36));
    assertThat(id3.length(), is(36));

    assertThat(id1, is(not(id2)));
    assertThat(id1, is(not(id3)));
    assertThat(id2, is(not(id3)));
  }
}