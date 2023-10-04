package org.foi.nwtis.vtvrdy.aplikacija_3.dretve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.vtvrdy.aplikacija_3.slusaci.SlusacAplikacije;
import org.foi.nwtis.vtvrdy.aplikacija_3.zrna.JmsPosiljatelj;

/**
 * Dretva koja sakuplja letove aviona
 * 
 * @author Veronika Tvrdy
 */
public class SakupljacLetovaAviona extends Thread {

  private DataSource ds;
  private JmsPosiljatelj jmsPosiljatelj;
  private int trajanjeCiklusa;
  private long preuzimanjeOd;
  private long preuzimanjeDo;
  private long zadnjiDan;
  private String korime;
  private String lozinka;
  private List<String> popisAerodroma;

  public SakupljacLetovaAviona(JmsPosiljatelj jmsPosiljatelj, DataSource ds) {
    this.ds = ds;
    this.jmsPosiljatelj = jmsPosiljatelj;
  }


  @Override
  public synchronized void start() {
    super.start();
  }

  @Override
  public void run() {
    preuzmiPostavke();
    zadnjiDan = dohvatiZadnjiDan();
    if (zadnjiDan == 0) {
      zadnjiDan = preuzimanjeOd;
    } else if (zadnjiDan >= preuzimanjeOd && zadnjiDan < preuzimanjeDo) {
      zadnjiDan = dohvatiIduciDan(zadnjiDan);
    } else if (zadnjiDan < preuzimanjeOd) {
      zadnjiDan = preuzimanjeOd;
    } else if (zadnjiDan == preuzimanjeDo) {
      System.out.println("Zadnji dan je : " + dohvatiDatumOdLong(zadnjiDan));
      zadnjiDan = 0;
    }
    if (zadnjiDan != 0) {
      System.out.println("Zadnji dan je : " + dohvatiDatumOdLong(zadnjiDan));
      while (zadnjiDan <= preuzimanjeDo) {
        System.out.println("Ulazak u petlju " + dohvatiDatumOdLong(zadnjiDan));
        long pocetak = System.currentTimeMillis();
        int brojLetova = dohvatiBrojLetova(zadnjiDan);
        String datum = dohvatiDatumOdLong(zadnjiDan);
        jmsPosiljatelj
            .saljiPoruku("Na dan: " + datum + " preuzeto ukupno " + brojLetova + " letova aviona");
        System.out
            .println("Na dan: " + datum + " preuzeto ukupno " + brojLetova + " letova aviona");
        zadnjiDan = dohvatiIduciDan(zadnjiDan);
        long kraj = System.currentTimeMillis();
        odradiSpavanje((kraj - pocetak) / 1000);
      }
    }
  }

  private String dohvatiDatumOdLong(long zadnjiDan2) {
    Date datum = new Date(zadnjiDan2 * 1000);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(datum);
  }


  /**
   *
   * Preuzimanje postavki definiranih u konfiguracijskoj datoteci
   */
  private void preuzmiPostavke() {
    korime = SlusacAplikacije.konfig.dajPostavku("OSKlijentBP.korisnik");
    lozinka = SlusacAplikacije.konfig.dajPostavku("OSKlijentBP.lozinka");
    preuzimanjeOd = dohvatiLongOdDana(SlusacAplikacije.konfig.dajPostavku("preuzimanje.od"));
    preuzimanjeDo = dohvatiLongOdDana(SlusacAplikacije.konfig.dajPostavku("preuzimanje.do"));
    trajanjeCiklusa = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("ciklus.trajanje"));
  }

  /**
   *
   * Dohvaća datum zadnje pohranjenog zapisa iz tablice
   */
  private long dohvatiZadnjiDan() {
    String query = "SELECT STORED FROM LETOVI_POLASCI ORDER BY STORED DESC LIMIT 1";
    long posljednji = 0;
    PreparedStatement stat = null;
    ResultSet rs = null;

    try (Connection con = ds.getConnection()) {
      stat = con.prepareStatement(query);
      rs = stat.executeQuery();
      if (rs.next()) {
        Timestamp timestamp = rs.getTimestamp("STORED");
        posljednji = timestamp.getTime() / 1000;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stat != null) {
          stat.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return posljednji;
  }

  private long dohvatiLongOdDana(String datum) {
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    Date dan = null;
    try {
      dan = df.parse(datum);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return dan.getTime() / 1000;
  }

  private long dohvatiIduciDan(long uvecajDan) {
    return uvecajDan += 86400;
  }

  /**
   *
   * Dohvaćanje broja spremljenih letova na određeni datum
   */

  private int dohvatiBrojLetova(long zaDan) {
    int brojac = 0;
    OSKlijentBP osKlijent = new OSKlijentBP(korime, lozinka);
    long danDo = zaDan + 86400;
    popisAerodroma = dohvatiAerodromeZaPreuzimanje();
    for (String icao : popisAerodroma) {
      List<LetAviona> letovi = new ArrayList<>();
      try {
        letovi = osKlijent.getDepartures(icao, zaDan, danDo);
        if (letovi != null) {
          System.out.println(
              "Za aerodrom: " + icao + " preuzeto ukupno " + letovi.size() + " letova aviona");
          for (LetAviona let : letovi) {
            if (spremiLet(let)) {
              brojac++;
            }
          }
        }
      } catch (NwtisRestIznimka e) {
        e.printStackTrace();
      }
    }
    return brojac;
  }

  /**
   *
   * Dohvaća aerodrome iz baze podataka za koje se preuzimaju letvi
   */
  private List<String> dohvatiAerodromeZaPreuzimanje() {
    String query = "SELECT ICAO FROM AERODROMI_LETOVI WHERE AKTIVAN = TRUE";
    List<String> aerodromi = new ArrayList<>();
    PreparedStatement stat = null;
    ResultSet rs = null;

    try (Connection con = ds.getConnection()) {
      stat = con.prepareStatement(query);
      rs = stat.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        aerodromi.add(icao);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stat != null) {
          stat.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return aerodromi;
  }


  /**
   *
   * Spremanje leta u bazu podataka
   */
  private boolean spremiLet(LetAviona let) {
    boolean uspjeh = false;
    String query = "INSERT INTO LETOVI_POLASCI (ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, "
        + "ESTARRIVALAIRPORT, CALLSIGN, ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, "
        + "ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, "
        + "DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    Timestamp timestamp = new Timestamp(zadnjiDan * 1000);
    PreparedStatement stmt = null;
    try (Connection con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, let.getIcao24());
      stmt.setInt(2, let.getFirstSeen());
      stmt.setString(3, let.getEstDepartureAirport());
      stmt.setInt(4, let.getLastSeen());
      stmt.setString(5, let.getEstArrivalAirport());
      stmt.setString(6, let.getCallsign());
      stmt.setInt(7, let.getEstDepartureAirportHorizDistance());
      stmt.setInt(8, let.getEstDepartureAirportVertDistance());
      stmt.setInt(9, let.getEstArrivalAirportHorizDistance());
      stmt.setInt(10, let.getEstArrivalAirportVertDistance());
      stmt.setInt(11, let.getDepartureAirportCandidatesCount());
      stmt.setInt(12, let.getArrivalAirportCandidatesCount());
      stmt.setTimestamp(13, timestamp);;
      stmt.executeUpdate();
      uspjeh = true;
    } catch (SQLException e) {
      if (!(e instanceof java.sql.SQLIntegrityConstraintViolationException)) {
        e.printStackTrace();
      }
      uspjeh = false;
    } finally {
      try {
        if (stmt != null && !stmt.isClosed()) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return uspjeh;
  }

  private void odradiSpavanje(Long rad) {
    Long spavanje = trajanjeCiklusa - rad;
    if (spavanje > 0) {
      try {
        sleep(spavanje * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void interrupt() {
    super.interrupt();
  }

}
