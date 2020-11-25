package var.sockets.udp.messwerte;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;

public class MesswertClient {

  private static final String HOSTNAME = "localhost";
  private static final int PORT = 4117;
  private static final int BUFSIZE = 512;

  public static void main(String[] args) {
    Random rg = new Random();
    try (DatagramSocket socket = new DatagramSocket()){
      InetAddress iaddr = InetAddress.getByName(HOSTNAME);
      DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE, iaddr, PORT);
      while (true) {
        String jetzt = (new Date()).toString();
        String messung = Double.toString(rg.nextDouble() * 100.0);
        packetOut.setData((jetzt +" "+ messung).getBytes());
        socket.send(packetOut);
        Thread.sleep(5000);
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
