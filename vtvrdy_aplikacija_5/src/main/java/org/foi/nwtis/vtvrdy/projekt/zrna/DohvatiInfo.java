package org.foi.nwtis.vtvrdy.projekt.zrna;

import org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije;

/**
 * Klasa za dohvaćanje informacija za prikaz u zaglavlju
 * 
 * @author Veronika Tvrdy
 */
public class DohvatiInfo {

  public DohvatiInfo() {}

  public Object getIme() {
    return SlusacAplikacije.konfig.dajPostavku("autor.ime");
  }

  public Object getPrezime() {
    return SlusacAplikacije.konfig.dajPostavku("autor.prezime");
  }

  public Object getPredmet() {
    return SlusacAplikacije.konfig.dajPostavku("autor.predmet");
  }

  public Object getGodina() {
    return SlusacAplikacije.konfig.dajPostavku("aplikacija.godina");
  }

  public Object getVerzija() {
    return SlusacAplikacije.konfig.dajPostavku("aplikacija.verzija");
  }

  public Integer getBrojRedova() {
    return Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranica.brojRedova"));
  }



}
