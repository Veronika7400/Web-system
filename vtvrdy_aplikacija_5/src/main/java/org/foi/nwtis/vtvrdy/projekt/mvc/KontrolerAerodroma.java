/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.vtvrdy.projekt.mvc;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsAerodromi.endpoint.Aerodrom;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.vtvrdy.projekt.rest.RestKlijentAerodroma;
import org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije;
import org.foi.nwtis.vtvrdy.projekt.zrna.DohvatiInfo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Veronika Tvrdy
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://webpredmeti:8080/vtvrdy_aplikacija_4/aerodromi?wsdl")
  private Aerodromi service;

  @WebServiceRef(wsdlLocation = "http://webpredmeti:8080/vtvrdy_aplikacija_4/meteo?wsdl")
  private Meteo serviceM;

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  /**
   * Dodavanje aerodroma na popis za preuzimanje letova
   *
   */
  @GET
  @Path("dodaj/{icao}")
  @View("registracijaOdgovor.jsp")
  public void dodajAerodrom(@PathParam("icao") String icao) {
    try {
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsAerodromiPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        var uspjesno = port.dodajAerodromZaLetove(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao);
        if (uspjesno) {
          poruka = "Dodavanje uspješno!";
        } else {
          poruka = "Dodavanje nije uspješno!";
        }
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }

      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje svih aerodroma
   *
   */
  @GET
  @Path("svi")
  @View("sviAerodromi.jsp")
  public void getSviAerodromi(@QueryParam("traziNaziv") @DefaultValue("") String traziNaziv,
      @QueryParam("traziDrzavu") @DefaultValue("") String traziDrzavu,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var aerodromi = rca.getSveAerodrome(traziNaziv, traziDrzavu, odBroja, broj);
      model.put("aerodromi", aerodromi);
      model.put("drzava", traziDrzavu);
      model.put("naziv", traziNaziv);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje jednog aerodroma
   *
   */
  @GET
  @Path("pregled/{icao}")
  @View("aerodrom.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var aerodrom = rca.getAerodrom(icao);
      model.put("aerodrom", aerodrom);

      var port = serviceM.getWsMeteoPort();
      var meteo = port.dajMeteo(icao);
      model.put("meteo", meteo);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje aerodroma za preuzimanje
   *
   */
  @GET
  @Path("zaPreuzimanje")
  @View("zaPreuzimanje.jsp")
  public void getAerodromeZaPreuzimanje() {
    try {
      dohvatiInfo();
      String poruka = null;
      List<Aerodrom> aerodromiZaLetove = new ArrayList<>();
      List<Aerodrom> aerodromiZaLetovePauza = new ArrayList<>();
      var port = service.getWsAerodromiPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        aerodromiZaLetove = port.dajAerodromeZaLetove(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka());
        aerodromiZaLetovePauza =
            port.dajAerodromeZaLetovePauza(SlusacAplikacije.getKorisnik().getKorime(),
                SlusacAplikacije.getKorisnik().getLozinka());
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }
      model.put("aerodromiZaLetove", aerodromiZaLetove);
      model.put("aerodromiZaLetovePauza", aerodromiZaLetovePauza);
      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pauziranje aerodroma za preuzimanje
   *
   */
  @GET
  @Path("zaPreuzimanje/pauziraj/{icao}")
  @View("registracijaOdgovor.jsp")
  public void getAerodromeZaPreuzimanjePauziraj(@PathParam("icao") String icao) {
    try {
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsAerodromiPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        var uspjesno = port.pauzirajAerodromZaLetove(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao);
        if (uspjesno) {
          poruka = "Pauziranje uspješno!";
        } else {
          poruka = "Pauziranje nije uspješno!";
        }
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }

      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Aktiviranje aerodroma za preuzimanje
   *
   */
  @GET
  @Path("zaPreuzimanje/aktiviraj/{icao}")
  @View("registracijaOdgovor.jsp")
  public void getAerodromeZaPreuzimanjeAktiviraj(@PathParam("icao") String icao) {
    try {
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsAerodromiPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        var uspjesno = port.aktivirajAerodromZaLetove(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao);
        if (uspjesno) {
          poruka = "Aktiviranje uspješno!";
        } else {
          poruka = "Aktiviranje nije uspješno!";
        }
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }

      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje udaljenosti dva aerodroma
   *
   */
  @GET
  @Path("aerodromiUdaljenosti")
  @View("aerodromiUdaljenosti.jsp")
  public void getAerodromiUdaljenost(@QueryParam("icaoOd") @DefaultValue("LDZA") String icaoFrom,
      @QueryParam("icaoDo") @DefaultValue("LOWW") String icaoTo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var udaljenosti = rca.getUdaljenostiIzmeđuDvaAerodoma(icaoFrom, icaoTo);
      model.put("udaljenosti", udaljenosti);
      model.put("icaoOd", icaoFrom);
      model.put("icaoDo", icaoTo);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Izračunavanje udaljenosti dva aerodroma
   *
   */
  @GET
  @Path("udaljenostDva")
  @View("aerodromiUdaljenostIzracunaj.jsp")
  public void getAerodromiUdaljenostIzracunaj(
      @QueryParam("icaoOd") @DefaultValue("LDZA") String icaoFrom,
      @QueryParam("icaoDo") @DefaultValue("LOWW") String icaoTo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var udaljenost = rca.getUdaljenostiIzmeđuDvaAerodomaIzracunaj(icaoFrom, icaoTo);
      model.put("udaljenost", udaljenost);
      model.put("icaoOd", icaoFrom);
      model.put("icaoDo", icaoTo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje manjih udaljenosti s obzirom na državu odredišnog aerodroma
   *
   */
  @GET
  @Path("udaljenostOdredisna")
  @View("aerodromiUdaljenostiOdredisna.jsp")
  public void getAerodromiUdaljenostOdredisna(
      @QueryParam("icaoOd") @DefaultValue("LDZA") String icaoFrom,
      @QueryParam("icaoDo") @DefaultValue("LOWW") String icaoTo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var udaljenosti = rca.getUdaljenostiIzmeđuDvaAerodomaOdredisna(icaoFrom, icaoTo);
      model.put("udaljenosti", udaljenosti);
      model.put("icaoOd", icaoFrom);
      model.put("icaoDo", icaoTo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dohvaćanje manjih udaljenosti s obzirom na odabranu državu
   *
   */
  @GET
  @Path("udaljenostOdabrana")
  @View("aerodromiUdaljenostiOdabrana.jsp")
  public void getAerodromiUdaljenostOdabrana(@QueryParam("icao") @DefaultValue("LDZA") String icao,
      @QueryParam("drzava") @DefaultValue("AT") String drzava,
      @QueryParam("km") @DefaultValue("150") Float km) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      dohvatiInfo();
      var udaljenosti = rca.getUdaljenostiIzmeđuDvaAerodomaOdabrana(icao, drzava, km);
      model.put("udaljenosti", udaljenosti);
      model.put("icao", icao);
      model.put("drzava", drzava);
      model.put("km", km);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void dohvatiInfo() {
    DohvatiInfo rca = new DohvatiInfo();
    var infoIme = rca.getIme();
    var infoPrezime = rca.getPrezime();
    var infoPredmet = rca.getPredmet();
    var infoGodina = rca.getGodina();
    var infoVerzija = rca.getVerzija();
    model.put("infoIme", infoIme);
    model.put("infoPrezime", infoPrezime);
    model.put("infoPredmet", infoPredmet);
    model.put("infoGodina", infoGodina);
    model.put("infoVerzija", infoVerzija);

  }
}
