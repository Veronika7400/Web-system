package org.foi.nwtis.vtvrdy.projekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;

/**
 * Ova klasa je zadužena za dretvenu obradu zahtjeva
 * 
 * @author vtvrdy
 *
 */

public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  private String naziv = "";
  GlavniPosluzitelj glavniPosluzitelj;
  private boolean kraj = false;
  public static String regExStatus = "STATUS";
  public static Pattern patternRegExStatus = Pattern.compile(regExStatus);
  public static String regExKraj = "KRAJ";
  public static Pattern patternRegExKraj = Pattern.compile(regExKraj);
  public static String regExInit = "INIT";
  public static Pattern patternRegExInit = Pattern.compile(regExInit);
  public static String regExPauza = "PAUZA";
  public static Pattern patternRegExPauza = Pattern.compile(regExPauza);
  public static String regExInfoDa = "INFO DA";
  public static Pattern patternRegExInfoDa = Pattern.compile(regExInfoDa);
  public static String regExInfoNe = "INFO NE";
  public static Pattern patternRegExInfoNe = Pattern.compile(regExInfoNe);
  public static String regExUdaljenost =
      "UDALJENOST ([+-]?(90(?:\\.\\d+)?|\\d+(?:\\.\\d+)?)) ([+-]?(180(?:\\.\\d+)?|\\d+(?:\\.\\d+)?)) ([+-]?(90(?:\\.\\d+)?|\\d+(?:\\.\\d+)?)) ([+-]?(180(?:\\.\\d+)?|\\d+(?:\\.\\d+)?))";
  public static Pattern patternRegExUdaljenost = Pattern.compile(regExUdaljenost);

  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig, String naziv,
      GlavniPosluzitelj glavniPosluzitelj) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
    this.naziv = naziv;
    this.glavniPosluzitelj = glavniPosluzitelj;
  }

  @Override
  public synchronized void start() {
    super.start();
  }

  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      this.mreznaUticnica.shutdownInput();
      String odgovor = this.obradiZahtjev(poruka.toString());
      if (glavniPosluzitelj.isIspis()) {
        System.out.println(poruka.toString());
      }
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
      GlavniPosluzitelj.aktivneDretve.remove(naziv);
      if (kraj) {
        glavniPosluzitelj.zavrsiRad();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Obrada pristiglog zahtjeva
   */
  private String obradiZahtjev(String komanda) {
    String odgovor = "";
    if (patternRegExStatus.matcher(komanda).matches()) {
      odgovor = "OK " + glavniPosluzitelj.getStatusPosluzitelja();
    } else if (patternRegExKraj.matcher(komanda).matches()) {
      odgovor = "OK";
      kraj = true;
    } else if (patternRegExInit.matcher(komanda).matches()) {
      if (!aktivanPosluzitelj()) {
        odgovor = obradiInitZahtjev();
      } else {
        odgovor = "ERROR 02 Poslužitelj je već u aktivnom stanju. ";
      }
    } else if (patternRegExPauza.matcher(komanda).matches()) {
      if (aktivanPosluzitelj()) {
        odgovor = obradiPauzaZahtjev(komanda);
      } else {
        odgovor = "ERROR 01 Poslužitelj je u stanju pauze. ";
      }
    } else if (patternRegExInfoDa.matcher(komanda).matches()) {
      if (aktivanPosluzitelj()) {
        odgovor = provjetiIspisujeLiVecDa(komanda);
      } else {
        odgovor = "ERROR 01 Poslužitelj je u stanju pauze. ";
      }
    } else if (patternRegExInfoNe.matcher(komanda).matches()) {
      if (aktivanPosluzitelj()) {
        odgovor = provjetiIspisujeLiVecNe(komanda);
      } else {
        odgovor = "ERROR 01 Poslužitelj je u stanju pauze. ";
      }
    } else if (patternRegExUdaljenost.matcher(komanda).matches()) {
      if (aktivanPosluzitelj()) {
        odgovor = izracunajUdaljenost(komanda);
      } else {
        odgovor = "ERROR 01 Poslužitelj je u stanju pauze. ";
      }
    } else {
      odgovor = "ERROR 05 Komanda nije u ispravnom formatu. " + komanda;
    }
    return odgovor;
  }

  private String provjetiIspisujeLiVecDa(String komanda) {
    if (!ispisujeLiNaIzlaz()) {
      return obradiInfoDa(komanda);
    } else {
      return "ERROR 03 Poslužitelj već ispisuje podatke o svom radu na standardni izlaz. ";
    }
  }

  private String provjetiIspisujeLiVecNe(String komanda) {
    if (ispisujeLiNaIzlaz()) {
      return obradiInfoNe(komanda);
    } else {
      return "ERROR 04 Poslužitelj već ne ispisuje podatke o svom radu na standardni izlaz. ";
    }
  }

  private boolean ispisujeLiNaIzlaz() {
    return glavniPosluzitelj.isIspis() ? true : false;
  }

  private boolean aktivanPosluzitelj() {
    return glavniPosluzitelj.getStatusPosluzitelja() == 1 ? true : false;
  }

  private String obradiInfoNe(String komanda) {
    glavniPosluzitelj.setIspis(false);
    return "OK";
  }

  private String obradiInfoDa(String komanda) {
    glavniPosluzitelj.setIspis(true);
    return "OK";
  }

  private String obradiPauzaZahtjev(String komanda) {
    int broj = glavniPosluzitelj.getBrojacZahtjeva();
    glavniPosluzitelj.setStatusPosluzitelja(0);
    return "OK " + broj;
  }

  private String obradiInitZahtjev() {
    glavniPosluzitelj.setStatusPosluzitelja(1);
    glavniPosluzitelj.setBrojacZahtjeva(0);
    return "OK";
  }

  /**
   * Izracun udaljenosti izmedu aerodroma
   */
  private String izracunajUdaljenost(String komanda) {
    String[] razdvoji = komanda.split(" ");
    if (!provjeriIspravnostDuzine(razdvoji)) {
      return "ERROR 05 Pogreška kod upisanih parametara komande udaljenosti!";
    }

    double udaljenostKilometri;
    var lat1 = Double.parseDouble(razdvoji[1]);
    var long1 = Double.parseDouble(razdvoji[2]);
    var lat2 = Double.parseDouble(razdvoji[3]);
    var long2 = Double.parseDouble(razdvoji[4]);
    final int R = 6371;

    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    Double latUdaljenost = Math.toRadians(lat2 - lat1);
    Double longUdaljenost = Math.toRadians(long2 - long1);

    Double a = Math.pow(Math.sin(latUdaljenost / 2), 2)
        + Math.pow(Math.sin(longUdaljenost / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    Double c = 2 * Math.asin(Math.sqrt(a));
    Double d = R * c;
    udaljenostKilometri = Math.round(d * 100.0) / 100.0;
    glavniPosluzitelj.setBrojacZahtjeva(glavniPosluzitelj.getBrojacZahtjeva() + 1);
    return "OK " + udaljenostKilometri;
  }

  private boolean provjeriIspravnostDuzine(String[] args) {
    return args.length == 5 ? true : false;
  }

  @Override
  public void interrupt() {
    super.interrupt();
  }

}
