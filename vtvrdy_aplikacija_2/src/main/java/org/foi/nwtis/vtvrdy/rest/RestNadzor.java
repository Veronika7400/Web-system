package org.foi.nwtis.vtvrdy.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.vtvrdy.slusaci.SlusacAplikacije;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 *
 * @author Veronika Tvrdy
 */

@Path("nadzor")
@RequestScoped
public class RestNadzor {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  private static final String BASE_URI = "http://200.20.0.1:8080/vtvrdy_aplikacija_2/api";
  private final Client client = ClientBuilder.newClient();
  private final WebTarget webTarget = client.target(BASE_URI).path("dnevnik");

  /**
   * Spremanje zahtjeva u tablicu dnevnik
   */
  private void spremiDnevnik(Dnevnik d) throws ClientErrorException {
    WebTarget resource = webTarget;
    Invocation.Builder request = resource.request();
    Response response = request.post(Entity.entity(d, MediaType.APPLICATION_JSON));
  }

  /**
   * Rest servis za dohvaćanje statusa poslužitelja
   *
   * @return the response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajStatus() {
    Response rez;
    String odgovor = posaljiKomandu("STATUS");
    rez = provjeriOdgovor(odgovor);
    Dnevnik d = new Dnevnik("AP2", "STATUS", null);
    spremiDnevnik(d);
    return rez;
  }

  /**
   * Rest servis za INIT | KRAJ | PAUZA poslužitelja
   *
   * @return the response
   */
  @GET
  @Path("{komanda}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajKomandu(@PathParam("komanda") String komanda) {
    Response rez;

    if (komandaIspravna(komanda)) {
      String odgovor = posaljiKomandu(komanda);
      rez = provjeriOdgovor(odgovor);
      Dnevnik d = new Dnevnik("AP2", komanda, null);
      spremiDnevnik(d);
      if (komandaInit(komanda)) {
        SlusacAplikacije.setAktivan(true);
      }
      if (komandaPauza(komanda)) {
        SlusacAplikacije.setAktivan(false);
      }
    } else {
      return Response.noContent().build();
    }
    return rez;
  }

  /**
   * Rest servis za INFO DA | INFO NE poslužitelja
   *
   * @return the response
   */
  @GET
  @Path("INFO/{vrsta}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajInfo(@PathParam("vrsta") String vrsta) {
    Response rez;


    if (komandaIspravnaInfo(vrsta)) {
      String odgovor = posaljiKomandu("INFO " + vrsta);
      rez = provjeriOdgovor(odgovor);
      Dnevnik d = new Dnevnik("AP2", "INFO " + vrsta, null);
      spremiDnevnik(d);
    } else {
      return Response.noContent().build();
    }
    return rez;
  }

  private Response provjeriOdgovor(String odgovor) {
    Gson gson = new Gson();
    if (odgovor.contains("OK")) {
      String podaci = gson.toJson(odgovor);
      return Response.status(Status.OK).entity(podaci).build();
    } else {
      String podaci = gson.toJson(odgovor);
      return Response.status(Status.OK).entity(podaci).build();
    }
  }

  private boolean komandaIspravnaInfo(String vrsta) {
    return vrsta.equals("DA") || vrsta.equals("NE") ? true : false;
  }

  private boolean komandaIspravna(String komanda) {
    return komanda.equals("INIT") || komanda.equals("KRAJ") || komanda.equals("PAUZA") ? true
        : false;
  }

  private boolean komandaInit(String komanda) {
    return komanda.equals("INIT") ? true : false;
  }

  private boolean komandaPauza(String komanda) {
    return komanda.equals("PAUZA") ? true : false;
  }

  /**
   * Metoda za slanje komande na poslužitelj
   *
   */
  private String posaljiKomandu(String komanda) {
    try {
      String adresa = SlusacAplikacije.konfig.dajPostavku("Adresa");
      int mreznaVrata = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("MreznaVrata"));
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String odgovor = komanda;
      pisac.write(odgovor);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }

      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
      return poruka.toString();
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}
