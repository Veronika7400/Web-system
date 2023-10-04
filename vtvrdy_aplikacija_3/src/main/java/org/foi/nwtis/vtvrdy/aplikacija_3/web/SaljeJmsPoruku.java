package org.foi.nwtis.vtvrdy.aplikacija_3.web;

import java.io.IOException;
import org.foi.nwtis.vtvrdy.aplikacija_3.zrna.JmsPosiljatelj;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Klasa koja šalje JMS poruku
 * 
 * @author Veronika Tvrdy
 */
@WebServlet(name = "SaljeJmsPoruku", urlPatterns = {"/SaljeJmsPoruku"})
public class SaljeJmsPoruku extends HttpServlet {

  private static final long serialVersionUID = 3187303834142572235L;
  @EJB
  JmsPosiljatelj jmsPosiljatelj;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var poruka = req.getParameter("poruka");
    if (poruka != null && !poruka.isEmpty()) {
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        System.out.println("Poruka je poslana!");
        return;
      }
      System.out.println("Greska kod slanja JMS poruke!");
      return;
    }
    System.out.println("poruka nema sadržaja!");
    return;
  }

}
