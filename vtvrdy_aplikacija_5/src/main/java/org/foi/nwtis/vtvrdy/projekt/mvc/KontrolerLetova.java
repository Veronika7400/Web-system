
package org.foi.nwtis.vtvrdy.projekt.mvc;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsLetovi.endpoint.LetAviona;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsLetovi.endpoint.Letovi;
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
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Veronika Tvrdy
 */
@Controller
@Path("letovi")
@RequestScoped
public class KontrolerLetova {

  @WebServiceRef(wsdlLocation = "http://webpredmeti:8080/vtvrdy_aplikacija_4/letovi?wsdl")
  private Letovi service;

  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  /**
   * Pregled spremljenih letova
   *
   */
  @GET
  @Path("pregledSpremljenih")
  @View("pregledSpremljenih.jsp")
  public void PregledSpremljenih(@QueryParam("icao") @DefaultValue("LDZA") String icao,
      @QueryParam("danOd") @DefaultValue("1.11.2022.") String danOd,
      @QueryParam("danDo") @DefaultValue("10.11.2022.") String danDo,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      List<LetAviona> polasci = new ArrayList<>();
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsLetoviPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        polasci = port.dajPolaskeInterval(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao, danOd, danDo, odBroja, broj);
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }
      model.put("icao", icao);
      model.put("danOd", danOd);
      model.put("danDo", danDo);
      model.put("polasci", polasci);
      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pregled spremljenih letova na dan
   *
   */
  @GET
  @Path("pregledSpremljenihNaDan")
  @View("pregledSpremljenihNaDan.jsp")
  public void PregledSpremljenihNaDan(@QueryParam("icao") @DefaultValue("LDZA") String icao,
      @QueryParam("danOd") @DefaultValue("1.11.2022.") String danOd,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    try {
      List<LetAviona> polasci = new ArrayList<>();
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsLetoviPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        polasci = port.dajPolaskeNaDan(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao, danOd, odBroja, broj);
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }
      model.put("icao", icao);
      model.put("danOd", danOd);
      model.put("polasci", polasci);
      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pregled spremljenih letova na dan koristeći OSKlijentBP
   *
   */
  @GET
  @Path("pregledSpremljenihNaDanOS")
  @View("pregledSpremljenihNaDanOS.jsp")
  public void PregledSpremljenihNaDanOS(@QueryParam("icao") @DefaultValue("LDZA") String icao,
      @QueryParam("danOd") @DefaultValue("1.11.2022.") String danOd) {
    try {
      List<LetAviona> polasci = new ArrayList<>();
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsLetoviPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        polasci = port.dajPolaskeNaDanOS(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), icao, danOd);
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }
      model.put("icao", icao);
      model.put("danOd", danOd);
      model.put("polasci", polasci);
      model.put("poruka", poruka);
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
