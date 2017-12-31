package com.dlucia.moneytransfer;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

class DatabaseBuilder
{
  private String script;

  private DatabaseBuilder() {}

  static DatabaseBuilder aDatabase()
  {
    return new DatabaseBuilder();
  }

  DatabaseBuilder withScript(String script)
  {
    this.script = script;
    return this;
  }

  DataSource build()
  {
    DataSource dataSource = dataSource();
    createDatabase(dataSource);

    return dataSource;
  }

  private void createDatabase(DataSource dataSource)
  {
    try
    {
      File file = new File("src/main/resource/" + script);
      String absolutePath = file.getAbsolutePath();
      Connection connection = dataSource.getConnection();
      RunScript.execute(connection, new FileReader(absolutePath));

      connection.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("Unable to create Database. " + e.getMessage());
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
