package com.revolut.moneytransfer.domain.exception;

import java.sql.SQLException;

import static java.lang.String.format;

public class DatabaseException extends TransferException
{
  private static final long serialVersionUID = 3834453312365092041L;

  public DatabaseException(SQLException exception)
  {
    super(format("Trouble with database. %s", exception.getMessage()));
  }

  public DatabaseException(String message)
  {
    super(message);
  }
}
