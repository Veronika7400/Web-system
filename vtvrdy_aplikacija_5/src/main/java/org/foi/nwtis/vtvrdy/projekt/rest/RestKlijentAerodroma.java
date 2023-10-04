package org.foi.nwtis.vtvrdy.projekt.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author Veronika Tvrdy
 */
public class RestKlijentAerodroma {

  public RestKlijentAerodroma() {}

  /**
   * Metoda za poziv klijenta da dohvati sve aerodrome
   *
   */
  public List<Aerodrom> getSveAerodrome(String traziNaziv, String traziDrzavu, int odBroja,
      int broj) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] json_Aerodromi = rc.getSviAerodromi(traziNaziv, traziDrzavu, odBroja, broj);
    List<Aerodrom> aerodromi;

    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom aerodrom = rc.getAerodrom(icao);
    rc.close();
    return aerodrom;
  }

  /**
   * Metoda za poziv klijenta da dohvati udaljenosti između aerodroma
   *
   */
  public List<Udaljenost> getUdaljenostiIzmeđuDvaAerodoma(String icaoFrom, String icaoTo) {
    RestKKlijent rc = new RestKKlijent();
    Udaljenost[] json_Udaljenosti = rc.getUdaljenostiIzmeđuDvaAerodoma(icaoFrom, icaoTo);
    List<Udaljenost> udaljenosti;

    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }

  /**
   * Metoda za poziv klijenta da dohvati izračun udaljenosti između aerodroma
   *
   */
  public String getUdaljenostiIzmeđuDvaAerodomaIzracunaj(String icaoFrom, String icaoTo) {
    RestKKlijent rc = new RestKKlijent();
    String udaljenost = rc.getUdaljenostiIzmeđuDvaAerodomaIzracunaj(icaoFrom, icaoTo);

    rc.close();
    return udaljenost;
  }

  /**
   * Metoda za poziv klijenta da dohvati vse manje udaljenosti između aerodroma
   *
   */
  public List<UdaljenostAerodrom> getUdaljenostiIzmeđuDvaAerodomaOdredisna(String icaoFrom,
      String icaoTo) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] json_Udaljenosti =
        rc.getUdaljenostiIzmeđuDvaAerodomaOdredisna(icaoFrom, icaoTo);
    List<UdaljenostAerodrom> udaljenosti;

    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }

  /**
   * Metoda za poziv klijenta da dohvati vse manje udaljenosti između aerodroma u odabranoj državi
   *
   */
  public List<UdaljenostAerodrom> getUdaljenostiIzmeđuDvaAerodomaOdabrana(String icao,
      String drzava, Float km) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] json_Udaljenosti =
        rc.getUdaljenostiIzmeđuDvaAerodomaOdabrana(icao, drzava, km);
    List<UdaljenostAerodrom> udaljenosti;

    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }


  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = getUri();

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public Aerodrom getAerodrom(String icao) {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
      return aerodrom;
    }

    public Aerodrom[] getSviAerodromi(String traziNaziv, String traziDrzavu, int odBroja,
        int broj) {
      WebTarget resource = webTarget;
      resource = resource.queryParam("traziNaziv", new Object[] {traziNaziv});
      resource = resource.queryParam("traziDrzavu", new Object[] {traziDrzavu});
      resource = resource.queryParam("odBroja", new Object[] {odBroja});
      resource = resource.queryParam("broj", new Object[] {broj});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);
      return aerodromi;
    }

    public Udaljenost[] getUdaljenostiIzmeđuDvaAerodoma(String icaoFrom, String icaoTo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {icaoFrom, icaoTo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenosti = gson.fromJson(request.get(String.class), Udaljenost[].class);
      return udaljenosti;
    }

    public String getUdaljenostiIzmeđuDvaAerodomaIzracunaj(String icaoFrom, String icaoTo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icaoFrom}));
      resource = resource.path("izracunaj");
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icaoTo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      String udaljenost = gson.fromJson(request.get(String.class), String.class);
      return udaljenost.split(" ")[1];
    }

    public UdaljenostAerodrom[] getUdaljenostiIzmeđuDvaAerodomaOdredisna(String icaoFrom,
        String icaoTo) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icaoFrom}));
      resource = resource.path("udaljenost1");
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icaoTo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      return udaljenosti;
    }

    public UdaljenostAerodrom[] getUdaljenostiIzmeđuDvaAerodomaOdabrana(String icao, String drzava,
        Float km) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      resource = resource.path("udaljenost2");
      resource = resource.queryParam("drzava", new Object[] {drzava});
      resource = resource.queryParam("km", new Object[] {km});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      return udaljenosti;
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
