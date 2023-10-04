package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy Klasa Korisnik
 */
@AllArgsConstructor()
public class Korisnik {

  @Getter
  @Setter
  private String ime;
  @Getter
  @Setter
  private String prezime;
  @Getter
  @Setter
  private String mail;
  @Getter
  @Setter
  private String korime;
  @Getter
  @Setter
  private String lozinka;

  public Korisnik() {}
}
