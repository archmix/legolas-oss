package legolas.sql.interfaces;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataSet extends ArrayList<DataObject> {
  private DataSet() {
    super();
  }

  public static DataSet empty() {
    return new DataSet();
  }

  public static DataSet create(ResultSet resultSet) {
    try {
      DataSet dataSet = new DataSet();
      ResultSetMetaData metaData = resultSet.getMetaData();
      Integer numberOfColumns = metaData.getColumnCount();

      DataObject dataObject = new DataObject();
      while (resultSet.next()) {
        for (int index = 1; index <= numberOfColumns; index++) {
          String property = metaData.getColumnName(index);
          Object value = resultSet.getObject(index);
          dataObject.set(property, value);
        }
        dataSet.add(dataObject);
      }

      return dataSet;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}