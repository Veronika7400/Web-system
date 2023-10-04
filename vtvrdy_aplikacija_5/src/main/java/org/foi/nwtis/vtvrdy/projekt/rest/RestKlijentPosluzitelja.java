package org.foi.nwtis.vtvrdy.projekt.rest;

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
public class RestKlijentPosluzitelja {

  public RestKlijentPosluzitelja() {}

  /**
   * Metoda za poziv klijenta da dohvati status posluzitelja
   *
   */
  public String getStatus() {
    RestKKlijent rc = new RestKKlijent();
    String status = rc.getStatus();
    rc.close();
    return status;
  }

  /**
   * Metoda za poziv klijenta da posalje komandu posluzitelju
   *
   */
  public String getKomanda(String komanda) {
    RestKKlijent rc = new RestKKlijent();
    String odg = rc.getKomanda(komanda);
    rc.close();
    return odg;
  }

  /**
   * Metoda za poziv klijenta da posalje komandu info posluzitelju
   *
   */
  public String getInfo(String komanda) {
    RestKKlijent rc = new RestKKlijent();
    String odg = rc.getInfo(komanda);
    rc.close();
    return odg;
  }

  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = getUri();

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    public String getInfo(String komanda) {
      WebTarget resource = webTarget;
      resource = resource.path("INFO");
      resource = resource.path(komanda);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      String odg = gson.fromJson(request.get(String.class), String.class);
      return odg;
    }

    public String getKomanda(String komanda) {
      WebTarget resource = webTarget;
      resource = resource.path(komanda);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      String odg = gson.fromJson(request.get(String.class), String.class);
      return odg;
    }

    public String getStatus() {
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Gson gson = new Gson();
      String status = gson.fromJson(request.get(String.class), String.class);
      return status;
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
