package org.foi.nwtis.podaci;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Dnevnik {
  @Getter
  @Setter
  private String vrsta;
  @Getter
  @Setter
  private String zahtjev;
  @Getter
  @Setter
  private Timestamp vrijeme;


  public Dnevnik() {}

}
