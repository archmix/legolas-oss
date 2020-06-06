package legolas.sql.interfaces;

public enum TargetDatabase {
  SQL_SERVER {
    @Override
    public String h2Mode() {
      return "MSSQLServer";
    }
  },
  ORACLE {
    @Override
    public String h2Mode() {
      return "Oracle";
    }
  },
  POSTGRESQL {
    @Override
    public String h2Mode() {
      return "PostgreSQL";
    }
  };

  public abstract String h2Mode();
}