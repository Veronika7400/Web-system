package org.foi.nwtis.vtvrdy.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import com.google.gson.Gson;


/**
 * Klasa za čitanje bin datoteke sa postavkama.
 * 
 * @author Veronika Tvrdy
 *
 */
public class KonfiguracijaJson extends KonfiguracijaApstraktna {
  /**
   * Konstanta TIP
   */
  public static final String TIP = "json";

  /**
   * Konstruktor
   * 
   * @param nazivDatoteke -naziv datoteke
   */
  public KonfiguracijaJson(String nazivDatoteke) {
    super(nazivDatoteke);
  }

  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);
    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije ispravnog tipa: '" + TIP + "'");
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće pisati u nju." + TIP);
    }

    try {
      Gson gson = new Gson();
      FileWriter fw = new FileWriter(datoteka);
      BufferedWriter bw = new BufferedWriter(fw);
      String json = gson.toJson(this.postavke);
      bw.write(json);
      bw.close();
      fw.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće pisati." + e.getMessage());
    }
  }

  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    var datoteka = this.nazivDatoteke;
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);
    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije ispravnog tipa: '" + TIP + "'");
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isReadable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće čitati iz nje." + TIP);
    }

    try {
      Gson gson = new Gson();
      FileReader fr = new FileReader(datoteka);
      BufferedReader br = new BufferedReader(fr);
      this.postavke = gson.fromJson(br, Properties.class);
      br.close();
      fr.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteku '" + datoteka + "' nije moguće čitati." + e.getMessage());
    }
  }
}
