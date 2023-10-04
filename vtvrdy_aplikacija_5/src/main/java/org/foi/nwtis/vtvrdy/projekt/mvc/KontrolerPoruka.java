/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.vtvrdy.projekt.mvc;

import org.foi.nwtis.vtvrdy.projekt.zrna.DohvatiInfo;
import org.foi.nwtis.vtvrdy.projekt.zrna.SakupljacJmsPoruka;
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
@Path("poruke")
@RequestScoped
public class KontrolerPoruka {

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  /**
   * Dohvaćanje poruka
   *
   */
  @GET
  @Path("porukeIzbornik")
  @View("pregledPoruka.jsp")
  public void poruke(@QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      dohvatiInfo();

      SakupljacJmsPoruka sakupljacJmsPoruka = SakupljacJmsPoruka.dohvatiInstancu();
      var poruke = sakupljacJmsPoruka.dohvatiPrimljenePoruke(odBroja, broj);
      model.put("poruke", poruke);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Brisanje poruka
   *
   */
  @GET
  @Path("porukeIzbornik/obrisi")
  @View("registracijaOdgovor.jsp")
  public void porukeObrisi() {
    try {
      dohvatiInfo();

      SakupljacJmsPoruka sakupljacJmsPoruka = SakupljacJmsPoruka.dohvatiInstancu();
      var poruke = sakupljacJmsPoruka.obrisiPoruke();
      model.put("poruke", poruke);

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
