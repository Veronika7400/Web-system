package org.foi.nwtis.vtvrdy.projekt.slusaci;

import java.io.File;
import org.foi.nwtis.KonfiguracijaBP;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;
import org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Veronika Tvrdy
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
  @Getter
  @Setter
  public static boolean aktivan = true;

  @Getter
  @Setter
  public static Korisnik korisnik;
  public static KonfiguracijaBP konfig;

  public SlusacAplikacije() {}

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    String nazivDatoteke = context.getInitParameter("konfiguracija");
    String putanja = context.getRealPath("/WEB-INF") + File.separator;
    nazivDatoteke = putanja + nazivDatoteke;

    konfig = new PostavkeBazaPodataka(nazivDatoteke);
    try {
      konfig.ucitajKonfiguraciju();
      context.setAttribute("postavke", konfig);
    } catch (NeispravnaKonfiguracija e) {
      e.printStackTrace();
      return;
    }
    ServletContextListener.super.contextInitialized(sce);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    context.removeAttribute("postavke");

    ServletContextListener.super.contextDestroyed(sce);
  }

}
