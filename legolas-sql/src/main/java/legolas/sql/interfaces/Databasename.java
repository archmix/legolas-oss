package legolas.sql.interfaces;

import legolas.docker.interfaces.Username;

public class Databasename {
  private static final Databasename valueOf = new Databasename();

  private final String value;

  public String value(){
    return this.value;
  }

  public static Databasename valueOf(){
    return valueOf;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public Databasename() {
    this.value = Username.valueOf().value().toLowerCase();
  }
}
