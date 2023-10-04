package org.foi.nwtis.vtvrdy.aplikacija_4.iznimka;

/**
 * Klasa iznimke za pogrešnu autentikaciju
 * 
 * @author Veronika Tvrdy
 */
public class PogresnaAutentikacija extends Exception {

  public PogresnaAutentikacija(String poruka) {
    super(poruka);
  }

}
