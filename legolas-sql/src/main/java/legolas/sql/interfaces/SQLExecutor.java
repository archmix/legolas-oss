package legolas.sql.interfaces;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "create")
public class SQLExecutor {
  private final DataSource dataSource;
  private Logger logger = LoggerFactory.getLogger(SQLExecutor.class);

  public boolean execute(String statement, Consumer<PreparedStatement> consumer) {
    Connection connection = null;
    PreparedStatement ps = null;

    try {
      connection = dataSource.getConnection();
      ps = connection.prepareStatement(statement);
      consumer.accept(ps);
      return ps.execute();
    } catch (SQLException e) {
      return report(e);
    } finally {
      close(ps);
      close(connection);
    }
  }

  public boolean execute(String statement) {
    Connection connection = null;
    Statement st = null;

    try {
      connection = dataSource.getConnection();
      st = connection.createStatement();
      return st.execute(statement);
    } catch (SQLException e) {
      return report(e);
    } finally {
      close(st);
      close(connection);
    }
  }

  public DataSet query(String statement) {
    Connection connection = null;
    Statement st = null;
    ResultSet rs = null;

    try {
      connection = dataSource.getConnection();
      st = connection.createStatement();
      rs = st.executeQuery(statement);

      return DataSet.create(rs);
    } catch (SQLException e) {
      report(e);
      return DataSet.empty();
    } finally {
      close(rs);
      close(st);
      close(connection);
    }
  }

  public DataSet query(String statement, Object... values) {
    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      connection = dataSource.getConnection();
      st = connection.prepareStatement(statement);
      int index = 1;
      for (Object value : values) {
        st.setObject(index++, value);
      }
      rs = st.executeQuery();
      return DataSet.create(rs);
    } catch (SQLException e) {
      report(e);
      return DataSet.empty();
    } finally {
      close(rs);
      close(st);
      close(connection);
    }
  }

  private boolean report(Exception e) {
    logger.warn("Error in sql execution", e);
    return false;
  }

  private void close(AutoCloseable connection) {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (Exception e) {
      logger.warn("Error on closing resource", e);
    }
  }
}
