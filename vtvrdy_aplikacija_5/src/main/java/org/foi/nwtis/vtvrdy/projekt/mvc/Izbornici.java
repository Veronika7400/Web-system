/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.vtvrdy.projekt.mvc;

import org.foi.nwtis.vtvrdy.projekt.rest.RestKlijentDnevnik;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author Veronika Tvrdy
 */
@Controller
@Path("izbornici")
@RequestScoped
public class Izbornici {

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  @GET
  @Path("korisniciIzbornik")
  @View("korisniciIzbornik.jsp")
  public void korisnici() {}

  @GET
  @Path("registracija")
  @View("registracija.jsp")
  public void registracija() {}

  @GET
  @Path("prijava")
  @View("prijava.jsp")
  public void prijava() {}

  @GET
  @Path("posluziteljIzbornik")
  @View("posluzitelj.jsp")
  public void posluzitelj() {}

  @GET
  @Path("aerodromiIzbornik")
  @View("aerodromiIzbornik.jsp")
  public void aerodromi() {
    RestKlijentDnevnik rcd = new RestKlijentDnevnik();
    dohvatiInfo(rcd);
  }

  @GET
  @Path("letoviIzbornik")
  @View("letoviIzbornik.jsp")
  public void letovi() {}

  @GET
  @Path("dnevnikIzbornik")
  @View("dnevnik.jsp")
  public void dnevnik(@QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj,
      @QueryParam("vrsta") @DefaultValue("") String vrsta) {
    try {
      RestKlijentDnevnik rcd = new RestKlijentDnevnik();
      dohvatiInfo(rcd);

      var dnevnik = rcd.getZapise(odBroja, broj, vrsta);
      model.put("dnevnik", dnevnik);
      model.put("filtar", vrsta);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void dohvatiInfo(RestKlijentDnevnik rcp) {
    var infoIme = rcp.getIme();
    var infoPrezime = rcp.getPrezime();
    var infoPredmet = rcp.getPredmet();
    var infoGodina = rcp.getGodina();
    var infoVerzija = rcp.getVerzija();
    var brojRedova = rcp.getBrojRedova();
    model.put("infoIme", infoIme);
    model.put("infoPrezime", infoPrezime);
    model.put("infoPredmet", infoPredmet);
    model.put("infoGodina", infoGodina);
    model.put("infoVerzija", infoVerzija);
    model.put("broj", brojRedova);

  }

}
