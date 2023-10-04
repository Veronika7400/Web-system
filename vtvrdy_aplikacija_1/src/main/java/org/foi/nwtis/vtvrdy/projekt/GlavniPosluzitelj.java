package org.foi.nwtis.vtvrdy.projekt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.NeispravnaKonfiguracija;
import lombok.Getter;
import lombok.Setter;


/**
 * Ova klasa je zadužena za otvaranje veze na određenim vratima
 * 
 * @author Veronika Tvrdy
 *
 */
public class GlavniPosluzitelj {

  public Konfiguracija konfig;
  private int mreznaVrata;
  private int brojDretvi;
  private static GlavniPosluzitelj glavniPosluzitelj;
  @Getter
  @Setter
  private int brojacZahtjeva = 0;
  @Getter
  @Setter
  private boolean ispis = false;

  public static Map<String, Thread> aktivneDretve = new HashMap<>();

  @Getter
  @Setter
  private int statusPosluzitelja = 0;
  ServerSocket posluzitelj;

  public GlavniPosluzitelj(Konfiguracija konfig) {
    this.konfig = konfig;
    glavniPosluzitelj = this;

  }

  private void dohvatiPostavke(Konfiguracija konf) throws NeispravnaKonfiguracija {
    this.mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    this.brojDretvi = Integer.parseInt(konf.dajPostavku("brojDretvi"));
  }

  private Boolean provjeriZauzetostVrata() {
    try {
      ServerSocket veza = new ServerSocket(this.mreznaVrata);
      veza.close();
      return false;
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * Pokretanje posluzitelja
   */
  public void pokreniPosluzitelja() {
    try {
      dohvatiPostavke(konfig);
      if (provjeriZauzetostVrata()) {
        System.out.println(
            "ERROR 05 Računalna vrata / port '" + this.mreznaVrata + "' su trenutno zauzeta.");
        return;
      } else {
        posluzitelj = new ServerSocket(this.mreznaVrata);
        pripremiPosluzitelja();
      }
    } catch (NeispravnaKonfiguracija e) {
      System.out.println("ERROR 05 Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
    } catch (IOException e) {
    }
  }

  /**
   * Priprema posluzitelja za rad s dretvama
   */
  private void pripremiPosluzitelja() throws IOException {

    var rbDretve = 0;
    while (statusPosluzitelja != 2) {
      Socket veza = posluzitelj.accept();
      if (this.brojDretvi > aktivneDretve.size()) {
        String naziv = "vtvrdy_" + rbDretve;
        rbDretve++;
        MrezniRadnik mr = new MrezniRadnik(veza, konfig, naziv, glavniPosluzitelj);
        aktivneDretve.put(naziv, mr);
        mr.setName(naziv);
        mr.start();
      }
    }

  }

  /**
   * Završetak rada poslužitelja.
   */
  public void zavrsiRad() {
    statusPosluzitelja = 2;
    if (!aktivneDretve.isEmpty()) {
      for (Thread dretva : aktivneDretve.values()) {
        dretva.interrupt();
      }
    }
    if (!posluzitelj.isClosed()) {
      try {
        posluzitelj.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}
