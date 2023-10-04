package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy Klasa Aerodrom
 */
@AllArgsConstructor()
public class Aerodrom {

  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private String naziv;
  @Getter
  @Setter
  private String drzava;
  @Getter
  @Setter
  private Lokacija lokacija;

  public Aerodrom() {}
}
