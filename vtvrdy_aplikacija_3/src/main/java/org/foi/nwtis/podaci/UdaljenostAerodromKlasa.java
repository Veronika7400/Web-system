package org.foi.nwtis.podaci;

/**
 *
 * @author Veronika Tvrdy Klasa UdaljenostAerodrom
 */
public class UdaljenostAerodromKlasa {

  private String icao;
  private float km;

  public UdaljenostAerodromKlasa(String icao, float km) {
    super();
    this.icao = icao;
    this.km = km;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public float getKm() {
    return km;
  }

  public void setKm(float km) {
    this.km = km;
  }

}
