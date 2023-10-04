package org.foi.nwtis.vtvrdy.slusaci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import org.foi.nwtis.KonfiguracijaBP;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.PostavkeBazaPodataka;
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

  public static KonfiguracijaBP konfig;

  @Getter
  @Setter
  public static boolean aktivan = false;

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
    if (provjeriStatusPosluzitelja()) {
      aktivan = true;
    } else {
      aktivan = false;
    }
  }


  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    context.removeAttribute("postavke");

    ServletContextListener.super.contextDestroyed(sce);
  }

  private boolean provjeriStatusPosluzitelja() {
    try {
      String adresa = konfig.dajPostavku("Adresa");
      int mreznaVrata = Integer.parseInt(konfig.dajPostavku("MreznaVrata"));
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String odgovor = "STATUS";
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
      if (poruka.toString().split(" ")[1].equals("1")) {
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      return false;
    }
  }

}
