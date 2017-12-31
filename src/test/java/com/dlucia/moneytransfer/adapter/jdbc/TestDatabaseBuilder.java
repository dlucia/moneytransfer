package com.dlucia.moneytransfer.adapter.jdbc;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import static org.h2.tools.RunScript.execute;

class TestDatabaseBuilder
{
  private String script;

  private TestDatabaseBuilder() {}

  static TestDatabaseBuilder aDatabase()
  {
    return new TestDatabaseBuilder();
  }

  TestDatabaseBuilder withScript(String script)
  {
    this.script = script;
    return this;
  }

  DataSource build()
  {
    DataSource dataSource = dataSource();
    try
    {
      createDatabase(dataSource);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unable to create Database. " + e.getMessage());
    }

    return dataSource;
  }

  private void createDatabase(DataSource dataSource) throws Exception
  {
    File file = new File("src/test/resource/" + script);
    try (Connection connection = dataSource.getConnection())
    {
      execute(connection, new FileReader(file));
    }
  }

  private static JdbcDataSource dataSource()
  {
    JdbcDataSource ds = new JdbcDataSource();
    ds.setURL("jdbc:h2:~/test");
    ds.setUser("test");
    return ds;
  }
}
