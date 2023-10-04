
package org.foi.nwtis.vtvrdy.projekt.mvc;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije;
import org.foi.nwtis.vtvrdy.projekt.zrna.DohvatiInfo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa KontrolerKorisnika
 * 
 * @author Veronika Tvrdy
 */
@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {

  @WebServiceRef(wsdlLocation = "http://webpredmeti:8080/vtvrdy_aplikacija_4/korisnici?wsdl")
  private Korisnici service;
  @Inject
  private Models model;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}


  /**
   * Registracija korisnika
   *
   */
  @POST
  @Path("registracija")
  @View("registracijaOdgovor.jsp")
  public void registrirajKorisnika(@FormParam("ime") String ime,
      @FormParam("prezime") String prezime, @FormParam("korime") String korime,
      @FormParam("mail") String mail, @FormParam("lozinka") String lozinka) {
    try {
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsKorisniciPort();
      Korisnik korisnik = new Korisnik();
      korisnik.setIme(ime);
      korisnik.setPrezime(prezime);
      korisnik.setKorime(korime);
      korisnik.setLozinka(lozinka);
      korisnik.setMail(mail);

      var uspjesno = port.dodajKorisnika(korisnik);
      if (uspjesno) {
        poruka = "Registracija uspješna!";
      } else {
        poruka = "Registracija nije uspješna!";
      }
      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Prijava korisnika
   *
   */
  @POST
  @Path("prijava")
  @View("registracijaOdgovor.jsp")
  public void prijaviKorisnika(@FormParam("korime") String korime,
      @FormParam("lozinka") String lozinka,
      @QueryParam("traziKorisnika") @DefaultValue("") String traziKorisnika) {
    try {
      dohvatiInfo();
      String poruka = null;
      var port = service.getWsKorisniciPort();
      Korisnik korisnik = port.dajKorisnika(korime, lozinka, korime);
      if (korisnik != null) {
        SlusacAplikacije.setKorisnik(korisnik);
        poruka = "Prijava uspješna!";
      } else {
        poruka = "Prijava nije uspješna!";
      }
      model.put("poruka", poruka);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pregled korisnika
   *
   */
  @GET
  @Path("pregledaj")
  @View("pregledajKorisnike.jsp")
  public void pregledajKorisnike(
      @QueryParam("traziImeKorisnika") @DefaultValue("") String traziImeKorisnika,
      @QueryParam("traziPrezimeKorisnika") @DefaultValue("") String traziPrezimeKorisnika) {
    try {
      dohvatiInfo();
      List<Korisnik> korisnici = new ArrayList<>();
      String poruka = null;
      var port = service.getWsKorisniciPort();
      if (SlusacAplikacije.getKorisnik() != null) {
        korisnici = port.dajKorisnike(SlusacAplikacije.getKorisnik().getKorime(),
            SlusacAplikacije.getKorisnik().getLozinka(), traziImeKorisnika, traziPrezimeKorisnika);
      } else {
        poruka = "Morate se prvo prijaviti u aplikaciju!";
      }

      model.put("poruka", poruka);
      model.put("korisnici", korisnici);
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
