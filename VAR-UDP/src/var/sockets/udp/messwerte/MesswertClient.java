package var.sockets.udp.messwerte;

import java.util.Date;
import java.util.Random;

public class MesswertClient {

  public static void main(String[] args) {
    Random rg = new Random();
    try {
      // ...
      while (true) {
        String jetzt = (new Date()).toString();
        String messung = Double.toString(rg.nextDouble() * 100.0);
        // ...
        Thread.sleep(5000);
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
