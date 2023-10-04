package org.foi.nwtis.vtvrdy.projekt.zrna;

import java.util.ArrayList;
import java.util.List;
import jakarta.ejb.Singleton;

/**
 * Klasa SakupljacJmsPoruka
 * 
 * @author Veronika Tvrdy
 */
@Singleton
public class SakupljacJmsPoruka {
  private List<String> primljenePoruke = new ArrayList<>();
  private static SakupljacJmsPoruka instance;

  /**
   * Metoda koja ce vratiti jedinstvenu instancu klase
   * 
   */
  public static SakupljacJmsPoruka dohvatiInstancu() {
    if (instance == null) {
      synchronized (SakupljacJmsPoruka.class) {
        if (instance == null) {
          instance = new SakupljacJmsPoruka();
        }
      }
    }
    return instance;
  }

  /**
   * Metoda za spremanje poruke u kolekciju
   * 
   */
  public void dodajPorukuKolekciji(String poruka) {
    primljenePoruke.add(poruka);
  }

  /**
   * Metoda za dohvaćanje svih poruka
   * 
   */

  public List<String> dohvatiPrimljenePoruke() {
    return primljenePoruke;
  }

  /**
   * Metoda za dohvaćanje svih poruka
   * 
   */

  public String obrisiPoruke() {
    primljenePoruke.clear();
    if (primljenePoruke.isEmpty()) {
      return "Poruke obrisane";
    } else {
      return "Poruke nisu obrisane";
    }

  }


  /**
   * Metoda za dohvaćanje svih poruka odBroja i broj
   * 
   */
  public List<String> dohvatiPrimljenePoruke(int odBroja, int broj) {
    List<String> rezultat = new ArrayList<>();
    int ukupanBrojPoruka = primljenePoruke.size();
    odBroja -= odBroja;

    if (odBrojaVanOpsega(odBroja)) {
      return rezultat;
    }

    int doBroja = odBroja + broj;
    if (doBroja > ukupanBrojPoruka) {
      doBroja = ukupanBrojPoruka;
    }

    rezultat.addAll(primljenePoruke.subList(odBroja, doBroja));

    return rezultat;
  }

  private boolean odBrojaVanOpsega(int odBroja) {
    int ukupanBrojPoruka = primljenePoruke.size();
    return odBroja < 0 || odBroja >= ukupanBrojPoruka ? true : false;
  }

}
