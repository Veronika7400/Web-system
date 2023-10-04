package org.foi.nwtis.vtvrdy.projekt;

import java.io.IOException;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Ova klasa je zadužena za pokretanje glavnog posluzitelja
 * 
 * @author vtvrdy
 *
 */

public class PokretacPosluzitelja {

  public static void main(String[] args) throws IOException {
    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args)) {
      System.out.println("Nije upisan naziv datoteke!");
      return;
    }

    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konf);
      System.out.println("Posluzitelj pokrenut!");
      glavniPosluzitelj.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija e) {
      System.out.println("Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
    }
  }

  private boolean provjeriArgumente(String[] args) {
    return args.length == 1 ? true : false;
  }

  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

}
