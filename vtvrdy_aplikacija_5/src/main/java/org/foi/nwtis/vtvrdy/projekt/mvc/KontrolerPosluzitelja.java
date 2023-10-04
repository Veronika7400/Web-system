/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.vtvrdy.projekt.mvc;

import org.foi.nwtis.vtvrdy.projekt.rest.RestKlijentPosluzitelja;
import org.foi.nwtis.vtvrdy.projekt.zrna.DohvatiInfo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 *
 * @author Veronika Tvrdy
 */
@Controller
@Path("posluzitelj")
@RequestScoped
public class KontrolerPosluzitelja {

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  /**
   * Dohvaćanje statusa poslužitelja
   *
   */
  @GET
  @Path("status")
  @View("posluzitelj.jsp")
  public void getStatus() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var status = rcp.getStatus();
      model.put("poruka", status);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Zaustavljanje poslužitelja
   *
   */
  @GET
  @Path("kraj")
  @View("posluzitelj.jsp")
  public void getKraj() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var kraj = rcp.getKomanda("KRAJ");
      model.put("poruka", kraj);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Inicijalizacija poslužitelja
   *
   */
  @GET
  @Path("init")
  @View("posluzitelj.jsp")
  public void getInit() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var init = rcp.getKomanda("INIT");
      model.put("poruka", init);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pauziranje poslužitelja
   *
   */
  @GET
  @Path("pauza")
  @View("posluzitelj.jsp")
  public void getPauza() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var pauza = rcp.getKomanda("PAUZA");
      model.put("poruka", pauza);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Neka info poslužitelja bude da
   *
   */
  @GET
  @Path("infoDa")
  @View("posluzitelj.jsp")
  public void getInfoDa() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var infoDa = rcp.getInfo("DA");
      model.put("poruka", infoDa);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Neka info poslužitelja bude da
   *
   */
  @GET
  @Path("infoNe")
  @View("posluzitelj.jsp")
  public void getInfoNe() {
    try {
      RestKlijentPosluzitelja rcp = new RestKlijentPosluzitelja();
      dohvatiInfo();
      var infoNe = rcp.getInfo("NE");
      model.put("poruka", infoNe);

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
