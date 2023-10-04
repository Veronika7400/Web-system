package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy Klasa Udaljenost
 */
@AllArgsConstructor()
public class Udaljenost {

  @Getter
  @Setter
  private String drzava;
  @Getter
  @Setter
  private Float km;

  public Udaljenost() {}
}
