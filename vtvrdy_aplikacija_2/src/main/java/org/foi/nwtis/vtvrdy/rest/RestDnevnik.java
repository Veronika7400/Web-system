package org.foi.nwtis.vtvrdy.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.vtvrdy.slusaci.SlusacAplikacije;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Veronika Tvrdy
 */

@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Rest servis za dohvaćanje podataka iz dnevnika
   *
   * @return the response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajPodatkeDnevnika(@QueryParam("vrsta") @DefaultValue("") String vrsta,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {

    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      List<Dnevnik> dnevnik = dohvatiPodatke(vrsta, odBroja - 1, broj);

      if (dnevnik.size() == 0) {
        return Response.noContent().build();
      }

      Gson gson = new Gson();
      String podaci = gson.toJson(dnevnik);
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    }
  }

  /**
   * Metoda za dohvaćanje podataka iz dnevnika
   *
   */
  private List<Dnevnik> dohvatiPodatke(String vrsta, int odBroja, int broj) {
    var dnevnikPodaci = new ArrayList<Dnevnik>();


    PreparedStatement stmt = null;

    try (Connection con = ds.getConnection();) {
      if (vrsta.isEmpty()) {
        String query = "select vrsta, zahtjev, vrijeme from DNEVNIK OFFSET ? LIMIT ?";
        stmt = con.prepareStatement(query);
        stmt.setInt(1, odBroja);
        stmt.setInt(2, broj);
      } else {
        String query =
            "select vrsta, zahtjev, vrijeme from DNEVNIK where vrsta = ? OFFSET ? LIMIT ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, vrsta);
        stmt.setInt(2, odBroja);
        stmt.setInt(3, broj);
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String vrstaUpita = rs.getString("vrsta");
        String zahtjev = rs.getString("zahtjev");
        Timestamp vrijeme = rs.getTimestamp("vrijeme");
        Dnevnik d = new Dnevnik(vrstaUpita, zahtjev, vrijeme);
        dnevnikPodaci.add(d);
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
    return dnevnikPodaci;
  }

  /**
   * Rest servis za dodavanje zapisa u dnevnik
   *
   * @return the response
   */

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response dodajStavkuDnevnika(Dnevnik stavka) {

    if (!SlusacAplikacije.isAktivan()) {
      Gson gson = new Gson();
      String podaci = gson.toJson("Posluzitelj nije inicijaliziran!");
      Response odgovor = Response.ok().entity(podaci).build();
      return odgovor;
    } else {
      if (stavka == null) {
        return Response.noContent().build();
      }
      Response odgovor = null;
      Integer rezultatUpita = pripremiUpit(stavka);
      if (rezultatUpita == 1) {
        odgovor = Response.ok().entity("Spremljeno").build();
      } else {
        odgovor = Response.ok().entity("Spremanje nije uspjelo").build();
      }
      return odgovor;
    }
  }

  /**
   * Metoda za dodavanje zapisa u dnevnik
   *
   */
  public Integer pripremiUpit(Dnevnik stavka) {
    String query = "INSERT INTO DNEVNIK (vrsta, zahtjev, vrijeme) VALUES (?, ?, CURRENT_TIMESTAMP)";

    Integer odg = 0;
    PreparedStatement stmt = null;
    try (Connection con = ds.getConnection();) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, stavka.getVrsta());
      stmt.setString(2, stavka.getZahtjev());
      odg = stmt.executeUpdate();

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
    return odg;
  }


}
