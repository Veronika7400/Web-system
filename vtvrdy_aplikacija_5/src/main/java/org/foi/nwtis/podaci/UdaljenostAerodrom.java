package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy Klasa UdaljenostAerodrom
 */
@AllArgsConstructor()
public class UdaljenostAerodrom {

  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private Float km;

  public UdaljenostAerodrom() {}
}
