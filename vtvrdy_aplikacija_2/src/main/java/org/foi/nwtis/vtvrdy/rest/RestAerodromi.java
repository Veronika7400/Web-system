package org.foi.nwtis.vtvrdy.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.vtvrdy.slusaci.SlusacAplikacije;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Veronika Tvrdy
 */

@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

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
    request.post(Entity.entity(d, MediaType.APPLICATION_JSON));
  }

  /**
   * Rest servis za dohvaćanje svih aerodroma
   *
   * @return the response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("traziNaziv") @DefaultValue("") String traziNaziv,
      @QueryParam("traziDrzavu") @DefaultValue("") String traziDrzavu,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      List<Aerodrom> aerodromi = dohvatiAerodrome(traziNaziv, traziDrzavu, odBroja - 1, broj);

      Dnevnik d = new Dnevnik("AP2", "Aerodromi: dajSveAerodrome", null);
      spremiDnevnik(d);
      if (aerodromi.size() == 0) {
        return Response.noContent().build();
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(aerodromi);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje svih aerodroma
   *
   */

  private List<Aerodrom> dohvatiAerodrome(String traziNaziv, String traziDrzavu, int odBroja,
      int broj) {
    List<Aerodrom> aerodromi = new ArrayList<>();

    try (Connection con = ds.getConnection();) {

      PreparedStatement stmt = pripremiUpit(traziNaziv, traziDrzavu, odBroja, broj, con);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String[] koordinate = rs.getString("COORDINATES").split(",");
        Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
        aerodromi.add(new Aerodrom(icao, naziv, drzava, lokacija));
      }
    } catch (SQLException e) {
      e.printStackTrace();

    }
    return aerodromi;
  }

  /**
   * Metoda za pripremu upita s obzirom na primljene parametre
   *
   */
  private PreparedStatement pripremiUpit(String traziNaziv, String traziDrzavu, int odBroja,
      int broj, Connection con) throws SQLException {
    PreparedStatement stmt = null;
    if (!traziNaziv.isEmpty() && !traziDrzavu.isEmpty()) {
      String query =
          "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS WHERE NAME LIKE ? AND ISO_COUNTRY = ? OFFSET ? LIMIT ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, "%" + traziNaziv + "%");
      stmt.setString(2, traziDrzavu);
      stmt.setInt(3, odBroja);
      stmt.setInt(4, broj);
    } else if (!traziNaziv.isEmpty() && traziDrzavu.isEmpty()) {
      String query =
          "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS WHERE NAME LIKE ? OFFSET ? LIMIT ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, "%" + traziNaziv + "%");
      stmt.setInt(2, odBroja);
      stmt.setInt(3, broj);
    } else if (traziNaziv.isEmpty() && !traziDrzavu.isEmpty()) {
      String query =
          "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS WHERE ISO_COUNTRY = ? OFFSET ? LIMIT ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, traziDrzavu);
      stmt.setInt(2, odBroja);
      stmt.setInt(3, broj);
    } else {
      String query = "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS OFFSET ? LIMIT ?";
      stmt = con.prepareStatement(query);
      stmt.setInt(1, odBroja);
      stmt.setInt(2, broj);
    }
    return stmt;
  }

  /**
   * Rest servis za dohvaćanje aerodroma
   *
   * @return the response
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      Aerodrom aerodrom = dohvatiAerodrom(icao);
      Dnevnik d = new Dnevnik("AP2", "Aerodromi: dajAerodrom ", null);
      spremiDnevnik(d);
      if (aerodrom == null) {
        return Response.noContent().build();
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(aerodrom);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje aerodroma
   *
   */
  private Aerodrom dohvatiAerodrom(String icaoAerodroma) {
    Aerodrom aerodrom = null;
    String query = "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS where ICAO = ?";

    PreparedStatement stmt = null;
    try (Connection con = ds.getConnection();) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoAerodroma);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String[] koordinate = rs.getString("COORDINATES").split(",");
        Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
        aerodrom = (new Aerodrom(icao, naziv, drzava, lokacija));
      }
    } catch (SQLException e) {
      e.printStackTrace();

    } finally {
      try {
        if (stmt != null && !stmt.isClosed()) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return aerodrom;
  }

  /**
   * Rest servis za dohvaćanje udaljenosti između dva aerodroma
   *
   * @return the response
   */
  @GET
  @Path("{icaoOd}/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuDvaAerodoma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      var udaljenosti = new ArrayList<Udaljenost>();
      Dnevnik d = new Dnevnik("AP2", "Aerodromi: daj udaljenosti između dva aerodoma", null);
      spremiDnevnik(d);
      udaljenosti.addAll(vratiUdaljenostiZaAerodrom(icaoFrom, icaoTo));

      Gson gson = new Gson();
      String podaci = gson.toJson(udaljenosti);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje udaljenosti za određeni aerodrom
   *
   */
  private ArrayList<Udaljenost> vratiUdaljenostiZaAerodrom(String icaoFrom, String icaoTo) {
    var udaljenosti = new ArrayList<Udaljenost>();
    String query = "select ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY from "
        + "AIRPORTS_DISTANCE_MATRIX where ICAO_FROM = ? AND ICAO_TO = ?";
    PreparedStatement stmt = null;

    try (Connection con = ds.getConnection();) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
      stmt.setString(2, icaoTo);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        Udaljenost u = new Udaljenost(drzava, udaljenost);
        udaljenosti.add(u);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed()) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return udaljenosti;
  }

  /**
   * Rest servis za dohvaćanje udaljenosti između svih aerodroma
   *
   * @return the response
   */
  @GET
  @Path("{icao}/udaljenosti")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuSvihAerodoma(@PathParam("icao") String icao,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      List<UdaljenostAerodrom> udaljenosti = dohvatiSveUdaljenosti(icao, odBroja - 1, broj);
      Dnevnik d =
          new Dnevnik("AP2", "Aerodromi udaljenosti: daj udaljenosti između svih aerodoma", null);
      spremiDnevnik(d);
      if (udaljenosti.size() == 0) {
        return Response.noContent().build();
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(udaljenosti);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje svih udaljenosti
   *
   */
  private List<UdaljenostAerodrom> dohvatiSveUdaljenosti(String icao, int odBroja, int broj) {
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();
    String query = "select ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY from "
        + "AIRPORTS_DISTANCE_MATRIX where ICAO_FROM = ? OFFSET ? LIMIT ?";
    PreparedStatement stmt = null;

    try (Connection con = ds.getConnection();) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      stmt.setInt(2, odBroja);
      stmt.setInt(3, broj);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icaoAerodroma = rs.getString("ICAO_TO");
        float udaljenost = rs.getFloat("DIST_CTRY");
        UdaljenostAerodrom u = new UdaljenostAerodrom(icaoAerodroma, udaljenost);
        udaljenosti.add(u);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed()) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return udaljenosti;
  }

  /**
   * Rest servis za izračun udaljenosti između dva aerodroma
   *
   * @return the response
   */
  @GET
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostKoristeciApp1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      Dnevnik d = new Dnevnik("AP2", "Aerodromi izracunaj: daj udaljenost koristeći app1", null);
      spremiDnevnik(d);
      String udaljenost = null;
      Aerodrom a1 = dohvatiAerodrom(icaoOd);
      Aerodrom a2 = dohvatiAerodrom(icaoDo);
      if (a1 != null && a2 != null) {
        String lat1 = a1.getLokacija().getLatitude();
        String long1 = a1.getLokacija().getLongitude();
        String lat2 = a2.getLokacija().getLatitude();
        String long2 = a2.getLokacija().getLongitude();
        udaljenost = posaljiKomanduApp1(lat1, long1, lat2, long2);
      }
      Gson gson = new Gson();
      String podaci = gson.toJson(udaljenost);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za slanje komande na poslužitelj
   *
   */
  private String posaljiKomanduApp1(String lat1, String long1, String lat2, String long2) {
    try {
      String adresa = SlusacAplikacije.konfig.dajPostavku("Adresa");
      int mreznaVrata = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("MreznaVrata"));
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String odgovor = "UDALJENOST " + lat1 + long1 + " " + lat2 + long2;
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
      return null;
    }
  }

  /**
   * Rest servis za dohvaćanje manjih udaljenosti između dva aerodroma
   *
   * @return the response
   */
  @GET
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveManjeUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      Dnevnik d = new Dnevnik("AP2", "Aerodromi udaljenost1: daj sve manje udaljenosti ", null);
      spremiDnevnik(d);
      List<UdaljenostAerodrom> rezultat = new ArrayList<UdaljenostAerodrom>();
      Aerodrom a1 = dohvatiAerodrom(icaoOd);
      Aerodrom a2 = dohvatiAerodrom(icaoDo);
      if (a1 != null && a2 != null) {
        String lat1 = a1.getLokacija().getLatitude();
        String long1 = a1.getLokacija().getLongitude();
        String lat2 = a2.getLokacija().getLatitude();
        String long2 = a2.getLokacija().getLongitude();
        String odgovor = posaljiKomanduApp1(lat1, long1, lat2, long2);

        Float udaljenost = Float.parseFloat(odgovor.split(" ")[1]);
        String drzava = a2.getDrzava();
        List<Aerodrom> listaDo = dohvatiAerodromeUDrzavi(drzava);
        rezultat.addAll(dohvatiSveManjeUdaljenosti(lat1, long1, listaDo, udaljenost));
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(rezultat);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje svih aerodroma u nekoj državi
   *
   */
  private List<Aerodrom> dohvatiAerodromeUDrzavi(String drzava2) {
    List<Aerodrom> aerodromi = new ArrayList<>();
    PreparedStatement stmt = null;
    try (Connection con = ds.getConnection();) {
      String query =
          "select ICAO, NAME, ISO_COUNTRY, COORDINATES from AIRPORTS WHERE ISO_COUNTRY = ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, drzava2);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String[] koordinate = rs.getString("COORDINATES").split(",");
        Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
        aerodromi.add(new Aerodrom(icao, naziv, drzava, lokacija));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return aerodromi;
  }

  /**
   * Rest servis za dohvaćanje manjih udaljenosti između dva aerodroma iz neke države
   *
   * @return the response
   */
  @GET
  @Path("{icaoOd}/udaljenost2")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveManjeUdaljenostiIzDrzave(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") Float km) {
    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {

      Dnevnik d =
          new Dnevnik("AP2", "Aerodromi udaljenost2: daj sve manje udaljenosti iz drzave", null);
      spremiDnevnik(d);
      List<UdaljenostAerodrom> rezultat = new ArrayList<UdaljenostAerodrom>();
      Aerodrom a1 = dohvatiAerodrom(icaoOd);

      if (a1 != null) {
        String lat1 = a1.getLokacija().getLatitude();
        String long1 = a1.getLokacija().getLongitude();
        List<Aerodrom> listaDo = dohvatiAerodromeUDrzavi(drzava);
        rezultat.addAll(dohvatiSveManjeUdaljenosti(lat1, long1, listaDo, km));
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(rezultat);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje manjih udaljenosti između dva aerodroma
   *
   */
  private List<UdaljenostAerodrom> dohvatiSveManjeUdaljenosti(String lat1, String long1,
      List<Aerodrom> listaDo, Float km) {
    List<UdaljenostAerodrom> rezultat = new ArrayList<>();
    for (Aerodrom a : listaDo) {
      Float u = Float.parseFloat(posaljiKomanduApp1(lat1, long1, a.getLokacija().getLatitude(),
          a.getLokacija().getLongitude()).split(" ")[1]);
      if (u < km) {
        UdaljenostAerodrom ua = new UdaljenostAerodrom(a.getIcao(), u);
        rezultat.add(ua);
      }
    }
    return rezultat;
  }

}
