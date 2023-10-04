package org.foi.nwtis.vtvrdy.projekt.zrna;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * Klasa za primanje JMS poruka
 * 
 * @author Veronika Tvrdy
 */
@MessageDriven(mappedName = "jms/NWTiS_vtvrdy",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")})
public class JmsPrimatelj implements MessageListener {
  SakupljacJmsPoruka sakupljacJmsPoruka = SakupljacJmsPoruka.dohvatiInstancu();

  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        var msg = (TextMessage) message;
        sakupljacJmsPoruka.dodajPorukuKolekciji(msg.getText());
        System.out.println("Stigla poruka:" + msg.getText());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}
