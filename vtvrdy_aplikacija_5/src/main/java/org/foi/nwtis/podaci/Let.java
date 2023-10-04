package org.foi.nwtis.podaci;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy
 */
@AllArgsConstructor()
public class Let {

  @Getter
  @Setter
  private String icao24;
  @Getter
  @Setter
  private int firstSeen;
  @Getter
  @Setter
  private String estDepartureAirport;
  @Getter
  @Setter
  private int lastSeen;
  @Getter
  @Setter
  private String estArrivalAirport;
  @Getter
  @Setter
  private String callSign;
  @Getter
  @Setter
  private int estDepartureAirportHorizDistance;
  @Getter
  @Setter
  private int estDepartureAirportVertDistance;
  @Getter
  @Setter
  private int estArrivalAirportHorizDistance;
  @Getter
  @Setter
  private int estArrivalAirportVertDistance;
  @Getter
  @Setter
  private int departureAirportCandidatesCount;
  @Getter
  @Setter
  private int arrivalAirportCandidatesCount;
  @Getter
  @Setter
  private Timestamp stored;

  public Let() {}
}
