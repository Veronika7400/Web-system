package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy Klasa Lokacija
 */
@AllArgsConstructor()
public class Lokacija {

  @Getter
  @Setter
  private String latitude;
  @Getter
  @Setter
  private String longitude;

  public Lokacija() {}

  public void postavi(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
