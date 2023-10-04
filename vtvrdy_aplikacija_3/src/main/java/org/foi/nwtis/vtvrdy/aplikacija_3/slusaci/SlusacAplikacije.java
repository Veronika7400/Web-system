package org.foi.nwtis.vtvrdy.aplikacija_3.slusaci;

import java.io.File;
import org.foi.nwtis.KonfiguracijaBP;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;
import org.foi.nwtis.vtvrdy.aplikacija_3.dretve.SakupljacLetovaAviona;
import org.foi.nwtis.vtvrdy.aplikacija_3.zrna.JmsPosiljatelj;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa SlusacAplikacije
 * 
 * @author Veronika Tvrdy
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
  @Getter
  @Setter
  public static boolean aktivan = true;

  @Inject
  JmsPosiljatelj jmsPosiljatelj;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  public static KonfiguracijaBP konfig;
  public SakupljacLetovaAviona sakupljacLetovaAviona = null;

  public SlusacAplikacije() {}

  /**
   * Inicijalizacija konteksta servleta
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("Ucitavanje");
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
    sakupljacLetovaAviona = new SakupljacLetovaAviona(jmsPosiljatelj, ds);
    sakupljacLetovaAviona.start();
    System.out.println("Dretva pokrenuta");
    ServletContextListener.super.contextInitialized(sce);
  }

  /**
   * Uništavanje konteksta servleta
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    sakupljacLetovaAviona.interrupt();
    context.removeAttribute("postavke");
    ServletContextListener.super.contextDestroyed(sce);
  }
}
