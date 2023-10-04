package org.foi.nwtis.vtvrdy.projekt.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author Veronika Tvrdy
 */
public class RestKlijentDnevnik {

  public RestKlijentDnevnik() {}

  /**
   * Metoda za poziv klijenta da dohvati zapise iz dnevnika
   *
   */
  public List<Dnevnik> getZapise(int odBroja, int broj, String vrsta) {
    RestKKlijent rc = new RestKKlijent();
    Dnevnik[] json_Dnevnik = rc.getZapise(odBroja, broj, vrsta);
    List<Dnevnik> dnevnik;
    if (json_Dnevnik == null) {
      dnevnik = new ArrayList<>();
    } else {
      dnevnik = Arrays.asList(json_Dnevnik);
    }
    rc.close();
    return dnevnik;


  }



  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = getUri();

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    /**
     * Metoda za dohvaćanje zapisa iz dnevnika
     *
     */
    public Dnevnik[] getZapise(int odBroja, int broj, String vrsta) {
      WebTarget resource = webTarget;
      resource = resource.queryParam("odBroja", new Object[] {odBroja});
      resource = resource.queryParam("broj", new Object[] {broj});
      resource = resource.queryParam("vrsta", new Object[] {vrsta});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Dnevnik[] dnevnik = gson.fromJson(request.get(String.class), Dnevnik[].class);
      return dnevnik;
    }

    public void close() {
      client.close();
    }

  }

  public Object getIme() {
    return SlusacAplikacije.konfig.dajPostavku("autor.ime");
  }

  public Object getPrezime() {
    return SlusacAplikacije.konfig.dajPostavku("autor.prezime");
  }

  public Object getPredmet() {
    return SlusacAplikacije.konfig.dajPostavku("autor.predmet");
  }

  public Object getGodina() {
    return SlusacAplikacije.konfig.dajPostavku("aplikacija.godina");
  }

  public Object getVerzija() {
    return SlusacAplikacije.konfig.dajPostavku("aplikacija.verzija");
  }

  public Integer getBrojRedova() {
    return Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranica.brojRedova"));
  }

  private static String getUri() {
    return SlusacAplikacije.konfig.dajPostavku("adresa.app2");
  }

}
